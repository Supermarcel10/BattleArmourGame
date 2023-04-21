package game.objects.tank;

import city.cs.engine.BodyImage;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;


public class Enemy extends Tank {
	public Player target;

	public Enemy(@NotNull TankType type, Vec2 position) {
		super(position);
		this.addImage(new BodyImage(type.image, halfSize * 2));
		this.setMaxHealth(type.health);
		this.speed = type.speed;
		this.scoreValue = type.scoreValue;
	}

	public static void traceToPlayer() {

	}

	public static void traceToBase() {

	}
}
