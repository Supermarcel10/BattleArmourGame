package game.prefab;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Tank;
import org.jbox2d.common.Vec2;


public class Player extends Tank {
	public Player(World world, Vec2 position) {
		super(world, position);
		attachBody();
		spawn();
	}

	public Player(World world, Vec2 position, Shape bodyShape) {
		super(world, position);
		attachBody(bodyShape);
		spawn();
	}

	public void spawn() {
		speed = 0.4f;
		setMaxHealth(1);
		body.addImage(new BodyImage(Config.image.get("player"), 3 * scaleFactor));
		super.spawn();
	}

	@Override
	public void update() {
		// Update the position of the player object based on the current movement direction
		position = position.add(moveDirection.mul(speed * scaleFactor));
		body.setPosition(position);

		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			float degrees = (float) (450 - Math.toDegrees(Math.atan2(moveDirection.y, moveDirection.x))) % 360;
			body.setAngle(degrees * (float) Math.PI / -180);
		}
//		body.setAngle(0 * (float) Math.PI / -180);
	}
}