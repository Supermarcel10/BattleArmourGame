package game.objects;

import city.cs.engine.Shape;
import city.cs.engine.World;
import game.prefab.Player;
import org.jbox2d.common.Vec2;


public class Enemy extends Tank {
	public Player target;

	public Enemy(World world, Vec2 position) {
		super(world, position);
	}

	public Enemy(World world, Vec2 position, Shape bodyShape) {
		super(world, position, bodyShape);
	}

	public Enemy(float speed, World world, Vec2 position) {
		super(speed, world, position);
	}

	public Enemy(float speed, World world, Vec2 position, Shape bodyShape) {
		super(speed, world, position, bodyShape);
	}
}
