package game.objects.abstractBody;

import city.cs.engine.*;
import game.IO.AM;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.MainGame.scaleFactor;
import static game.MainGame.scaledGridSize;
import static game.MainGame.world;


/**
 * Custom dynamic body class that extends DynamicBody.
 */
public class DynamicBody extends city.cs.engine.DynamicBody {
	public float speed;
	public Vec2 moveDirection = new Vec2(0, 0);

	public DynamicBody(Vec2 position) {
		super(world);
		setPosition(position);
	}

	public DynamicBody(float speed){
		super(world);
		this.speed = speed;
	}

	public DynamicBody(float speed, Vec2 position) {
		super(world);
		this.speed = speed;
		setPosition(position);
	}

	public DynamicBody(Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		setPosition(position);
	}

	public DynamicBody(float speed, Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		this.speed = speed;
		setPosition(position);
	}

	public void explode() {
		destroy();

		soundHandler.play(AM.tankSound.get("tankExplode"));
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

	/**
	 * Sets the position of the body using JBox2D positioning Vector2.
	 * @param position JBox2D Positioning Vector2
	 */
	public void setPositionJBox(@NotNull Vec2 position) {
		super.setPosition(position);
	}

	/**
	 * Gets the position of the body in grid positioning Vector2.
	 * @return Grid Positioning Vector2
	 */
	public Vec2 getPosition() {
		return new Vec2(super.getPosition().x / (scaledGridSize * 2 * scaleFactor),
				super.getPosition().y / (scaledGridSize * 2 * scaleFactor));
	}

	/**
	 * Gets the position of the body in JBox2D positioning Vector2.
	 * @return JBox2D Positioning Vector2
	 */
	public Vec2 getPositionJBox() {
		return super.getPosition();
	}

	/**
	 * Gets the degrees of a Vec2.
	 * @param vec The Vec2 to get the degrees of.
	 * @return The degrees of the Vec2.
	 */
	public float Vec2ToDegrees(@NotNull Vec2 vec) {
		return (float) (450 - Math.toDegrees(Math.atan2(vec.y, vec.x))) % 360;
	}

	/**
	 * Gets the Vec2 of a radian.
	 * @param radians The radian to get the Vec2 of.
	 * @return The Vec2 of the radian.
	 */
	public Vec2 radiansToVec2(float radians) {
		return new Vec2(-Math.round(Math.sin(radians)), Math.round(Math.cos(radians)));
	}

	/**
	 * Gets the Vec2 of a degree.
	 * @param degrees The degree to get the Vec2 of.
	 * @return The Vec2 of the degree.
	 */
	public Vec2 degToVec2(float degrees) {
		return radiansToVec2(degrees * (float) Math.PI / 180);
	}

	/**
	 * Method for overriding updating of the body.
	 */
	public void update() {}
}
