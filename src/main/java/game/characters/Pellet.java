package game.characters;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;


public class Pellet extends Body {
	public Pellet(float speed, World world, Vec2 position, Vec2 direction) {
		super(speed, world, position);
		spawn();
	}

	@Override
	public void spawn() {
		super.spawn();
		// implementation here
	}

	@Override
	public void collide() {
		// implementation here
	}
}
