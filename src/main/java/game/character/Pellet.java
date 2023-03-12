package game.character;

import city.cs.engine.*;
import game.character.abstractBody.Body;
import org.jbox2d.common.Vec2;


public class Pellet extends Body {
	public Pellet(float speed, World world, Vec2 position, Vec2 direction) {
		super(speed, world, position);
		spawn();
	}

	@Override
	public void spawn() {
		// implementation here

		super.spawn();
	}

	@Override
	public void collide() {
		// implementation here
	}
}
