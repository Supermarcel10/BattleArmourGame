package game.prefab;

import city.cs.engine.*;
import game.objects.Pickup;
import game.objects.Tank;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.util.HashMap;


public class Player extends Tank {
	public HashMap<PickupType, Integer[]> perks = new HashMap<>();

	public Player(Vec2 position) {
		super(position);

		speed = TankType.PLAYER.speed;
		setMaxHealth(TankType.PLAYER.health);
		this.addImage(new BodyImage(TankType.PLAYER.image, 3 * scaleFactor));
		scoreValue = TankType.PLAYER.scoreValue;

		spawn();
	}

	public void pickUp(Pickup pickup) {
		if (pickup.type.bulletCount != 0) {
			perks.put(pickup.type, new Integer[]{pickup.type.duration, pickup.type.bulletCount});
		} else {
			perks.put(pickup.type, new Integer[]{pickup.type.duration});
		}

		pickup.destroy();

		new Timer(pickup.type.duration * 1000, e -> {
			perks.remove(pickup.type);

			switch (pickup.type) {
				case QUAD_SHOT -> shotStyle.remove(ShotStyle.QUAD);
				case DOUBLE_DAMAGE -> shotDamage /= 2;
				case SPEED_BOOST -> speed -= 0.2f;
				case FAST_SHOT -> changeShootingDelay(500);
				case BULLET_PROPULSION -> shotSpeed = 150f;
				case PENETRATING_BULLETS -> availableShots.removeIf(shot -> shot.containsKey(ShotType.PENETRATING));
				case EXPLOSIVE_BULLETS -> availableShots.removeIf(shot -> shot.containsKey(ShotType.EXPLOSIVE));
			}

			((Timer) e.getSource()).stop();
		}).start();

		switch (pickup.type) {
			case QUAD_SHOT -> shotStyle.put(ShotStyle.QUAD, pickup.type.bulletCount);
			case DOUBLE_DAMAGE -> shotDamage *= 2;
			case SPEED_BOOST -> speed += 0.2f;
			case FAST_SHOT -> changeShootingDelay(350);
			case BULLET_PROPULSION -> shotSpeed = 300f;
			case PENETRATING_BULLETS -> availableShots.add(new HashMap<>(){{
				put(ShotType.PENETRATING, pickup.type.bulletCount);
			}});
			case EXPLOSIVE_BULLETS -> availableShots.add(new HashMap<>(){{
				put(ShotType.EXPLOSIVE, pickup.type.bulletCount);
			}});
		}
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
