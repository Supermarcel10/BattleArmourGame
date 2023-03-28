package game.objects;

import city.cs.engine.BodyImage;
import city.cs.engine.Shape;
import city.cs.engine.World;
import game.prefab.Player;
import game.prefab.enemies.EnemyType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;


public class Enemy extends Tank {
	public Player target;

	public Enemy(Vec2 position) {
		super(position);
	}

	public Enemy(@NotNull EnemyType type, Vec2 position) {
		super(position);
		this.addImage(new BodyImage(type.image, 3 * scaleFactor));
		this.setMaxHealth(type.health);
		this.speed = type.speed;
		this.scoreValue = type.scoreValue;
	}

	public static void traceToPlayer() {

	}

	public static void traceToBase() {

	}
}
