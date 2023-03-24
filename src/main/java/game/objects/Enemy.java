package game.objects;

import city.cs.engine.Shape;
import city.cs.engine.World;
import game.prefab.Player;
import org.jbox2d.common.Vec2;


public class Enemy extends Tank {
	public Player target;

	public Enemy(Vec2 position) {
		super(position);
	}

	public Enemy(Vec2 position, Shape bodyShape) {
		super(position, bodyShape);
	}

	public Enemy(float speed, Vec2 position) {
		super(speed, position);
	}

	public Enemy(float speed, Vec2 position, Shape bodyShape) {
		super(speed, position, bodyShape);
	}
}
