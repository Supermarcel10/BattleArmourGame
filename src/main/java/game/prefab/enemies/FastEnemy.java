package game.prefab.enemies;

import city.cs.engine.BodyImage;
import city.cs.engine.Shape;
import game.input.Config;
import game.objects.Enemy;
import org.jbox2d.common.Vec2;


public class FastEnemy extends Enemy {
	private static final String IMAGE = Config.image.get("fastEnemy");

	public FastEnemy(Vec2 position, Shape bodyShape) {
		super(position, bodyShape);
		spawn();
	}

	@Override
	public void spawn() {
		speed = 0.6f;
		scoreValue = 250;
		setMaxHealth(1);
		this.addImage(new BodyImage(IMAGE, 3 * scaleFactor));
		super.spawn();
	}
}
