package game.objects;

import city.cs.engine.World;
import org.jbox2d.common.Vec2;


public class Enemy extends Tank {
	public Enemy(World world, Vec2 position) {
		super(world, position);
	}

	public Enemy(float speed, World world, Vec2 position) {
		super(speed, world, position);
	}
}
