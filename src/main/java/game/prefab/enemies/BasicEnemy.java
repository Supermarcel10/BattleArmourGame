package game.prefab.enemies;

import city.cs.engine.BodyImage;
import city.cs.engine.Shape;
import city.cs.engine.World;
import game.input.Config;
import game.objects.Enemy;
import org.jbox2d.common.Vec2;


public class BasicEnemy extends Enemy {
	private static final String IMAGE = Config.image.get("basicEnemy");

	public BasicEnemy(World world, Vec2 position, Shape bodyShape) {
		super(world, position);
		attachBody(bodyShape);
		spawn();
	}

	@Override
	public void spawn() {
		speed = 0.4f;
		scoreValue = 100;
		setMaxHealth(1);
		body.addImage(new BodyImage(IMAGE, 3 * scaleFactor));
		super.spawn();
	}
}
