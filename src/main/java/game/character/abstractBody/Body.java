package game.character.abstractBody;

import city.cs.engine.BoxShape;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.input.Config.resolution;


public abstract class Body implements IBody {
	protected float speed = 8f;
	protected World world;
	public Vec2 position;
	protected Vec2 moveDirection = new Vec2(0, 0);
	protected DynamicBody body;
	protected Animation animation;

	protected float scaleFactor = resolution.x / 1920;


	public Body(World world, Vec2 position) {
		this.world = world;
		this.position = position;
	}

	public Body(float speed, World world, Vec2 position) {
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
		body.setPosition(position);
	}

	public void destroy() {
		body.destroy();
	}

	public void update() {}

	public void setMoveDirection(Vec2 direction) {
		moveDirection = direction;
	}

	public Vec2 getMoveDirection() {
		return moveDirection;
	}
}
