package game.prefab;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Tank;
import org.jbox2d.common.Vec2;


public class Player extends Tank {
	public Player(World world, Vec2 position) {
		super(world, position);
		spawn();
	}

	public Player(World world, Vec2 position, Shape bodyShape) {
		super(world, position, bodyShape);
		spawn();
	}

	public void spawn() {
		speed = 0.4f;
		setMaxHealth(1);
		this.addImage(new BodyImage(Config.image.get("player"), 3 * scaleFactor));
		scoreValue = -5000;
		super.spawn();
	}

	@Override
	public void update() {
		// Update the position of the player object based on the current movement direction
		setPosition(getPosition().add(moveDirection.mul(speed * scaleFactor)));

		// Movement smoothing
		float roundedX = ((float) Math.round(getPosition().x * 5) / 5.0f);
		float roundedY = ((float) Math.round(getPosition().y * 5) / 5.0f);
		this.setPosition(new Vec2(roundedX, roundedY));

		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			float degrees = (float) (450 - Math.toDegrees(Math.atan2(moveDirection.y, moveDirection.x))) % 360;
			this.setAngle(degrees * (float) Math.PI / -180);
		}
	}
}