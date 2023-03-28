package game.prefab;

import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import game.input.Config;
import game.objects.Enemy;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.main.Game.scaleFactor;
import static game.main.Game.scaledGridSize;


public enum TankType {
	PLAYER(1, 0.4f, -5000, Config.image.get("player")),
	BASIC(1, 0.4f, 100, Config.image.get("basicEnemy")),
	HEAVY(3, 0.3f, 300, Config.image.get("heavyEnemy")),
	FAST(1, 0.6f, 250, Config.image.get("fastEnemy")),
	EXPLODING(1, 0.5f, 250, Config.image.get("explodingEnemy"));

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
