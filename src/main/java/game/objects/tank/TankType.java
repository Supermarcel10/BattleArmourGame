package game.objects.tank;

import game.IO.AM;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;


public enum TankType {
	PLAYER(1, 0.4f, -5000, AM.image.get("player")),
	BASIC(1, 0.4f, 100, AM.image.get("basicEnemy")),
	HEAVY(3, 0.3f, 300, AM.image.get("heavyEnemy")),
	FAST(1, 0.6f, 250, AM.image.get("fastEnemy")),
	EXPLODING(1, 0.5f, 250, AM.image.get("explodingEnemy"));

	public final int health;
	public final float speed;
	public final int scoreValue;
	public final String image;

	TankType(int health, float speed, int scoreValue, String image) {
		this.health = health;
		this.speed = speed;
		this.scoreValue = scoreValue;
		this.image = image;
	}

	public @NotNull Enemy createEnemy(Vec2 pos) {
		return new Enemy(this, pos);
	}

	public @NotNull Player createPlayer(Vec2 pos) {
		return new Player(pos);
	}
}
