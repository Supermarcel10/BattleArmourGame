package game.objects.tank;

import city.cs.engine.BodyImage;
import game.objects.block.Block;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.MainGame.*;
import static java.lang.System.exit;


/**
 * The enemy tank class.
 */
public final class Enemy extends Tank {
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
		enemies.remove(this);
	}

	public void traceToPlayer() {

	}

	public void traceToBase() {

	}

	/**
	 * Updates the enemy tank with pathfinding, movement and shooting mechanics.
	 */
	public void update() {
//		// PATHFINDING
//		if (untilRecalculateUpdate == 0) {
//			untilRecalculateUpdate = RECALCULATE_PATH_RATE;
//			System.out.println("Recalculate update");
//		} else untilRecalculateUpdate--;
//
//		// MOVEMENT
//
//		// Angle the player towards the moving direction.
//		moveDirection = new Vec2(0, -1);
//
//		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
//			float degrees = (float) (450 - Math.toDegrees(Math.atan2(moveDirection.y, moveDirection.x))) % 360;
//			this.setAngle(degrees * (float) Math.PI / -180);
//		}
//
//		// SHOOTING
//		Vec2 pos = getPosition();
//		pos = new Vec2(pos.x + hGridSize, pos.y + hGridSize);
//		int blocksEncountered = 0;
//
//		while (blocksEncountered < 2) {
//			pos = pos.add(moveDirection);
//
//			if (!(pos.x >= 0 && pos.x <= gridSize && pos.y >= 0 && pos.y <= gridSize)) break;
//
//			if (blocks[(int) pos.x][(int) pos.y] != null) blocksEncountered++;
//
//			for (Player p : player) {
//				if (isWithinDistance(p.getPosition(), new Vec2(pos.x - hGridSize, pos.y - hGridSize), 2)) {
//					shoot();
//				}
//			}
//		}
	}
}
