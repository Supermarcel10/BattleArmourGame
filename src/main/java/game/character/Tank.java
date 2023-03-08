package game.character;

import city.cs.engine.World;
import game.character.abstractBody.Body;
import org.jbox2d.common.Vec2;

public class Tank extends Body {
	public Tank(World world, Vec2 position) {
		super(world, position);
	}

	public Tank(float speed, World world, Vec2 position) {
		super(speed, world, position);
	}
}
