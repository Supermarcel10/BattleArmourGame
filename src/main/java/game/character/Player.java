package game.character;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;


public class Player extends Tank {
	public Player(World world, Vec2 position) {
		super(world, position);
		attachBody();
		spawn();
	}

	public void spawn() {
		position = new Vec2(4 * scaleFactor,10 * scaleFactor);
		speed = 0.4f;
		super.spawn();
	}

	public void shoot() {
		new Pellet(speed, world, body.getPosition());
	}

	@Override
	public void update() {
		// Update the position of the player object based on the current movement direction
		position = position.add(moveDirection.mul(speed));
		body.setPosition(position);
	}
}