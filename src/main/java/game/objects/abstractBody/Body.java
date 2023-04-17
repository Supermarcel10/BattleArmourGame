package game.objects.abstractBody;

import city.cs.engine.*;
import game.main.Game;
import org.jbox2d.common.Vec2;


public abstract class Body extends DynamicBody implements IBody {
	public float speed = 8f;
	protected static World world = Game.world;
	protected static float scaleFactor = Game.scaleFactor;
	protected static float scaledGridSize = Game.scaledGridSize;
	protected Vec2 moveDirection = new Vec2(0, 0);

	public Body(Vec2 position) {
		super(world);
		setPosition(position);
	}

	public Body(float speed, Vec2 position) {
		super(world);
		this.speed = speed;
		setPosition(position);
	}

	public Body(Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		setPosition(position);
	}

	public Body(float speed, Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		this.speed = speed;
		setPosition(position);
	}

	public void spawn() {
		this.setPosition(new Vec2(((scaledGridSize * 2) * getPosition().x) * scaleFactor,
				((scaledGridSize * 2) * getPosition().y) * scaleFactor));
	}

	public void explode() {
		destroy();
		// TODO: Add explosion animation.
		// TODO: Add explosion sound.
		// TODO: Add explosion damage.
	}

	public Vec2 getGridPos() {
		return new Vec2((int) (getPosition().x / (scaledGridSize * 2 * scaleFactor)),
				(int) (getPosition().y / (scaledGridSize * 2 * scaleFactor)));
	}

	public Vec2 getJBoxPos() {
		return new Vec2(((scaledGridSize * 2) * getPosition().x) * scaleFactor,
				((scaledGridSize * 2) * getPosition().y) * scaleFactor);
	}

	public void setMoveDirection(Vec2 direction) {
		moveDirection = direction;
	}

	public Vec2 getMoveDirection() {
		return moveDirection;
	}

	public void update() {}
}
