package game.objects.abstractBody;

import city.cs.engine.*;
import game.main.Game;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.main.Game.scaledGridSize;


public abstract class Body extends DynamicBody implements IBody {
	protected float speed = 8f;
	protected World world;
	protected Vec2 moveDirection = new Vec2(0, 0);
	protected float scaleFactor = Game.scaleFactor;

	public Body(World world, Vec2 position) {
		super(world);
		this.world = world;
		setPosition(position);
	}

	public Body(float speed, World world, Vec2 position) {
		super(world);
		this.speed = speed;
		this.world = world;
		setPosition(position);
	}

	public Body(World world, Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		this.world = world;
		setPosition(position);
	}

	public Body(float speed, World world, Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		this.speed = speed;
		this.world = world;
		setPosition(position);
	}

	public void spawn() {
		this.setPosition(new Vec2(((scaledGridSize * 2) * getPosition().x) * scaleFactor, ((scaledGridSize * 2) * getPosition().y) * scaleFactor));
	}

	public void setMoveDirection(Vec2 direction) {
		moveDirection = direction;
	}

	public Vec2 getMoveDirection() {
		return moveDirection;
	}

	public void update() {}
}
