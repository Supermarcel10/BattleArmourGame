package game.objects.abstractBody;

import city.cs.engine.*;
import game.Game;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;


public abstract class Body extends DynamicBody implements IBody {
	public float speed;
	protected static World world = Game.world;
	protected static float scaleFactor = Game.scaleFactor;
	protected static float scaledGridSize = Game.scaledGridSize;
	public Vec2 moveDirection = new Vec2(0, 0);

	public Body(Vec2 position) {
		super(world);
		setPosition(position);
	}

	public Body(float speed){
		super(world);
		this.speed = speed;
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

	public void explode() {
		destroy();
		// TODO: Add explosion animation.
		// TODO: Add explosion sound.
		// TODO: Add explosion damage.
	}

	/**
	 * Sets the position of the body using grid positioning Vector2.
	 * @param position Grid Positioning Vector2
	 */
	public void setPosition(@NotNull Vec2 position) {
		super.setPosition(new Vec2(((scaledGridSize * 2) * position.x) * scaleFactor,
				((scaledGridSize * 2) * position.y) * scaleFactor));
	}

	public void setPositionJBox(@NotNull Vec2 position) {
		super.setPosition(position);
	}

	public Vec2 getPosition() {
		return new Vec2(super.getPosition());
	}

	public Vec2 getPositionJBox() {
		return super.getPosition();
	}

	public void update() {}
}
