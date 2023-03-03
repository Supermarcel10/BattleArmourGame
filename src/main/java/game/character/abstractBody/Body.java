package game.character.abstractBody;

import city.cs.engine.BoxShape;
import city.cs.engine.DynamicBody;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

import static game.Config.resolution;


public abstract class Body implements IBody {
	protected float speed = 8f;
	protected World world;
	protected Vec2 position;
	protected Vec2 moveDirection = new Vec2(0, 0);
	protected DynamicBody body;

	protected float scaleFactor = resolution.x / 1920;


	public Body(World world, Vec2 position) {
		this.world = world;
		this.position = position;
		body = new DynamicBody(world, new BoxShape(1 * scaleFactor,2 * scaleFactor));
	}

	public Body(float speed, World world, Vec2 position) {
		this.speed = speed;
		this.world = world;
		this.position = position;
		body = new DynamicBody(world, new BoxShape(1 * scaleFactor,2 * scaleFactor));
	}

	public void spawn() {
		body.setPosition(position);
	}

	public void update(long elapsedNanos) {}

	public void setMoveDirection(Vec2 direction) {
		moveDirection = direction;
	}

	public Vec2 getMoveDirection() {
		return moveDirection;
	}
}
