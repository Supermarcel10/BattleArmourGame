package game.objects.tank;

import game.IO.AM;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;


/**
 * An enum containing all the different types of tanks.
 */
public enum TankType {
	PLAYER(1, 2, -5000, AM.image.get("player")),
	BASIC(1, 2, 100, AM.image.get("basicEnemy")),
	HEAVY(3, 4, 300, AM.image.get("heavyEnemy")),
	FAST(1, 1, 250, AM.image.get("fastEnemy")),
	EXPLODING(1, 3, 250, AM.image.get("explodingEnemy"));

	public final int health;
	public final int updateFrequency;
	public final int scoreValue;
	public final String image;

	TankType(int health, int updateFrequency, int scoreValue, String image) {
		this.health = health;
		this.updateFrequency = updateFrequency;
		this.scoreValue = scoreValue;
		this.image = image;
	}

	/**
	 * Creates a new enemy of the given type.
	 * @param pos The grid Vec2 position of the enemy.
	 * @return The newly created enemy.
	 */
	public @NotNull Enemy createEnemy(Vec2 pos) {
		return new Enemy(this, pos);
	}

	/**
	 * Creates a new player.
	 * @param pos The grid Vec2 position of the player.
	 * @return The newly created player.
	 */
	public @NotNull Player createPlayer(Vec2 pos) {
		return new Player(pos);
	}
}
