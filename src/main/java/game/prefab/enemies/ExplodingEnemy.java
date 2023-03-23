package game.prefab.enemies;

import city.cs.engine.BodyImage;
import city.cs.engine.Shape;
import city.cs.engine.World;
import game.input.Config;
import game.objects.Enemy;
import org.jbox2d.common.Vec2;


public class ExplodingEnemy extends Enemy {
	private static final String IMAGE = Config.image.get("explodingEnemy");

	public ExplodingEnemy(World world, Vec2 position, Shape bodyShape) {
		super(world, position);
//		attachBody(bodyShape);
		spawn();
	}

	@Override
	public void shoot() {
		// TODO: Remove shooting, enable exploding.
	}

	@Override
	public void spawn() {
		speed = 0.5f;
		scoreValue = 250;
		setMaxHealth(1);
		this.addImage(new BodyImage(IMAGE, 3 * scaleFactor));
		super.spawn();
	}
}
