package game.objects;

import city.cs.engine.BodyImage;
import game.prefab.Player;
import game.prefab.TankType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;


public class Enemy extends Tank {
	public Player target;

	public Enemy(@NotNull TankType type, Vec2 position) {
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
