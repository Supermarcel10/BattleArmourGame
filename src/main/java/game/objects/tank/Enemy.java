package game.objects.tank;

import city.cs.engine.BodyImage;
import game.objects.block.Block;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.MainGame.*;


/**
 * The enemy tank class.
 */
public class Enemy extends Tank {
	private final static int RECALCULATE_PATH_RATE = 60;
	private int untilRecalculateUpdate = 0;
	public Player target;

	public Enemy(@NotNull TankType type, Vec2 position) {
		super(position);
		this.addImage(new BodyImage(type.image, halfSize * 2));
		this.setMaxHealth(type.health);
		this.speed = type.speed;
		this.scoreValue = type.scoreValue;
	}

	/**
	 * Destroys the {@link Enemy} and removes it from the {@link game.MainGame#enemies} list.
	 */
	public void destroy() {
		super.destroy();
		enemies.remove(this);
	}

	public void traceToPlayer() {

	}

	public void traceToBase() {

	}

	/**
	 * Updates the enemy tank with pathfinding, movement and shooting mechanics.
	 */
	public void update() {
	}
}
