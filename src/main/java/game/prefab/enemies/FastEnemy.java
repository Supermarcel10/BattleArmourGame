package game.prefab.enemies;

import city.cs.engine.BodyImage;
import city.cs.engine.Shape;
import city.cs.engine.World;
import game.input.Config;
import game.objects.Enemy;
import org.jbox2d.common.Vec2;


public class FastEnemy extends Enemy {
	private static final String IMAGE = Config.image.get("fastEnemy");

	public FastEnemy(World world, Vec2 position, Shape bodyShape) {
		super(world, position);
		attachBody(bodyShape);
		spawn();
	}

	@Override
	public void spawn() {
		speed = 0.6f;
		setMaxHealth(1);
		body.addImage(new BodyImage(IMAGE, 3 * scaleFactor));
		super.spawn();
	}
}
