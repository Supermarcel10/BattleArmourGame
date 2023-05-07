package game.objects.abstractBody;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.MainGame.*;


/**
 * Custom dynamic body class that extends DynamicBody.
 */
public abstract class DynamicBody extends city.cs.engine.DynamicBody {
	public float movePollingRate = 1;
	public float currentMovePoll = 0;
	public Vec2 moveDirection = new Vec2(0, 0);

	public DynamicBody(Vec2 position) {
		super(world);
		setPosition(position);
	}

	public DynamicBody(int movePollingRate){
		super(world);
		this.movePollingRate = movePollingRate;
	}

	public DynamicBody(int movePollingRate, Vec2 position) {
		super(world);
		this.movePollingRate = movePollingRate;
		setPosition(position);
	}

	public DynamicBody(Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		setPosition(position);
	}

	public DynamicBody(int movePollingRate, Vec2 position, Shape bodyShape) {
		super(world, bodyShape);
		this.movePollingRate = movePollingRate;
		setPosition(position);
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
	public void setPositionJBox(Vec2 position) {
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

	public void updateMovement() {}

	/**
	 * Method for overriding updating of the body.
	 */
	/**
	 * Update the tank object position and angle based on the current movement direction and speed.
	 */
	public void update() {
		if (currentMovePoll == 0) {
			updateMovement();
			currentMovePoll = movePollingRate;
		} else currentMovePoll--;

		// Angle the tank towards the moving direction.
		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			setAngle((float) (Vec2ToDegrees(moveDirection) * Math.PI / -180));
		}
	}
}
