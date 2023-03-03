package game.characters;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;


public class Player extends Body {
	public Player(World world, Vec2 position) {
		super(world, position);
		spawn();
	}

	public void spawn() {
		position = new Vec2(4 * scaleFactor,10 * scaleFactor);
//		body.addImage(image);

		super.spawn();
	}

	public void shoot() {}

	@Override
	public void update(long elapsedNanos) {
		// Update the position of the player object based on the current movement direction
		Vec2 position = body.getPosition();
		position = position.add(moveDirection.mul(speed * (elapsedNanos / 1000000000.0f)));
		body.setPosition(position);
	}
}