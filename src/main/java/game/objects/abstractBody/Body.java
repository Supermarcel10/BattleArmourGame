package game.objects.abstractBody;

import city.cs.engine.*;
import game.main.Game;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.main.Game.scaledGridSize;


public abstract class Body extends DynamicBody implements IBody {
	protected float speed = 8f;
	protected World world;
	public Vec2 position;
	protected Vec2 moveDirection = new Vec2(0, 0);
	protected DynamicBody body;
	protected float scaleFactor = Game.scaleFactor;

	public Body(World world, Vec2 position) {
		super(world);
		this.world = world;
		this.position = position;
	}

	public Body(float speed, World world, Vec2 position) {
		super(world);
		this.speed = speed;
		this.world = world;
		this.position = position;
	}

	public void attachBody() {
		body = new DynamicBody(world, new BoxShape(1 * scaleFactor,2 * scaleFactor));
	}

	public void attachBody(Shape shape) {
		body = new DynamicBody(world, shape);
	}

	public void attachBody(@NotNull Vec2 size) {
		body = new DynamicBody(world, new BoxShape(size.x * scaleFactor,size.y * scaleFactor));
	}

	public void spawn() {
		body.setPosition(new Vec2(((scaledGridSize * 2) * position.x) * scaleFactor, ((scaledGridSize * 2) * position.y) * scaleFactor));
	}

	public void destroy() {
		body.destroy();
	}

	public void setMoveDirection(Vec2 direction) {
		moveDirection = direction;
	}

	public Vec2 getMoveDirection() {
		return moveDirection;
	}

	public void update() {}
}
