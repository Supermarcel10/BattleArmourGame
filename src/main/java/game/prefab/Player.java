package game.prefab;

import city.cs.engine.*;
import game.objects.Tank;
import org.jbox2d.common.Vec2;


public class Player extends Tank {
	public Player(Vec2 position) {
		super(position);

		speed = TankType.PLAYER.speed;
		setMaxHealth(TankType.PLAYER.health);
		this.addImage(new BodyImage(TankType.PLAYER.image, 3 * scaleFactor));
		scoreValue = TankType.PLAYER.scoreValue;

		spawn();
	}

	@Override
	public void update() {
		// Get the updated player object position based on the current movement direction and speed.
		Vec2 newPosition = getPosition().add(moveDirection.mul(speed * scaleFactor));

		for (Body b : this.getBodiesInContact()) {
			for (Fixture f : b.getFixtureList()) {
				if (f.getBody() instanceof Tank) {
					 break;
				}

				if (f.intersects(newPosition, halfSize, halfSize)) {
					return;
				}
			}
		}

		// TODO: Consider smoothing out movement.
		// If no collisions occur, move the player.
		setPosition(newPosition);

		// Angle the player towards the moving direction.
		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			float degrees = (float) (450 - Math.toDegrees(Math.atan2(moveDirection.y, moveDirection.x))) % 360;
			this.setAngle(degrees * (float) Math.PI / -180);
		}
	}
}
