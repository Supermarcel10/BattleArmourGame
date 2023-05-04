package game.objects.tank;

import city.cs.engine.BodyImage;
import game.objects.block.Block;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static game.MainGame.*;


/**
 * The enemy tank class.
 */
public class Enemy extends Tank {
	private final static int RECALCULATE_PATH_RATE = 60;
	private int untilRecalculateUpdate = 0;
	public Player target;

	public Enemy(@NotNull TankType type, Vec2 position) {
		super(position);
		this.addImage(new BodyImage(type.image, halfSize * 2));
		this.setMaxHealth(type.health);
		this.speed = type.speed;
		this.scoreValue = type.scoreValue;
	}

	/**
	 * Destroys the {@link Enemy} and removes it from the {@link game.MainGame#enemies} list.
	 */
	public void destroy() {
		super.destroy();
	}

	private void shootIfPlayerInSight() {
		Vec2 lookDirection = radiansToVec2(getAngle());

		if (canShoot && !Objects.equals(lookDirection, new Vec2(0, 0))) {
			Vec2 pos = getPosition();
			pos = new Vec2(pos.x + hGridSize, pos.y + hGridSize);
			int blocksEncountered = 0, distance = 0;

			while (blocksEncountered < 2) {
				distance++;
				pos = pos.add(lookDirection);

				if (!(pos.x >= 0 && pos.x <= gridSize && pos.y >= 0 && pos.y <= gridSize)) break;

				Block b = blocks[(int) pos.x][(int) pos.y];

				if (b != null && b.type.isSolid) {
					if (b.type.damageable) blocksEncountered++;
					else return;
				}

				for (Player p : player) {
					// If the player is not in the game, dead or not moving: skip
					if (p == null || p.health <= 0 || Objects.equals(p.moveDirection, new Vec2(0, 0))) continue;

					// Check if the player is within shooting conditions.
					if (isWithinDistance(p.getPosition(), new Vec2(pos.x - hGridSize, pos.y - hGridSize), 1 + (distance / 10f))) {
						shoot();
					}
				}
			}
		}
	}

	/**
	 * Updates the enemy tank with pathfinding, movement and shooting mechanics.
	 */
	public void update() {
		// PATHFINDING
		if (untilRecalculateUpdate == 0) {
			untilRecalculateUpdate = RECALCULATE_PATH_RATE;
//			System.out.println("Recalculate update");
		} else untilRecalculateUpdate--;

		// MOVEMENT
		moveDirection = new Vec2(0, -1);
		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			setAngle((float) (Vec2ToDegrees(moveDirection) * Math.PI / -180));
		}

		// SHOOTING
		shootIfPlayerInSight();
	}
}
