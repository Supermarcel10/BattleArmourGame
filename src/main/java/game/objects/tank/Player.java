package game.objects.tank;

import city.cs.engine.*;
import game.objects.pickup.Pickup;
import game.objects.pickup.PickupType;
import game.objects.shot.Shot;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;

import static game.MainGame.scaleFactor;


/**
 * The player class.
 */
public class Player extends Tank {
	public HashMap<PickupType, Integer[]> perks = new HashMap<>();

	public Player(Vec2 position) {
		super(position);

		speed = TankType.PLAYER.speed;
		setMaxHealth(TankType.PLAYER.health);
		this.addImage(new BodyImage(TankType.PLAYER.image, halfSize * 2));
		scoreValue = TankType.PLAYER.scoreValue;
	}

	/**
	 * Handle pickup logic.
	 * @param pickup The pickup object to be picked up.
	 */
	public void pickUp(@NotNull Pickup pickup) {
		// Add the perk to the perks list.
		if (pickup.type.bulletCount != 0) {
			perks.put(pickup.type, new Integer[]{pickup.type.duration, pickup.type.bulletCount});
		} else {
			perks.put(pickup.type, new Integer[]{pickup.type.duration});
		}

		// Remove the pickup from the world.
		pickup.destroy();

		new Timer(pickup.type.duration * 1000, e -> {
			// Remove the perk from the perks list.
			perks.remove(pickup.type);

			// Remove the perk from the player.
			pickup.type.removePerk(this);

			((Timer) e.getSource()).stop();
		}).start();

		// Apply the perk to the player.
		pickup.type.applyPerk(this);
	}

	/**
	 * Update the player object position and angle based on the current movement direction and speed.
	 */
	@Override
	public void update() {
		// Get the updated player object position based on the current movement direction and speed.
		Vec2 newPosition = getPositionJBox().add(moveDirection.mul(speed * scaleFactor));

		for (Body b : this.getBodiesInContact()) {
			for (Fixture f : b.getFixtureList()) {
				if (f.getBody() instanceof Shot || f.getBody() instanceof Pickup) {
					 break;
				}

				if (f.intersects(newPosition, halfSize, halfSize)) {
					return;
				}
			}
		}

		// TODO: Consider smoothing out movement.
		// If no collisions occur, move the player.
		setPositionJBox(newPosition);

		// Angle the player towards the moving direction.
		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			float degrees = (float) (450 - Math.toDegrees(Math.atan2(moveDirection.y, moveDirection.x))) % 360;
			this.setAngle(degrees * (float) Math.PI / -180);
		}
	}
}
