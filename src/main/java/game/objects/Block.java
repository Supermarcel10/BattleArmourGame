package game.objects;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import static game.main.Game.*;


public class Block extends StaticBody {
	private final BodyImage image;
	protected boolean damageable = false;
	protected int maxHealth = 1;
	protected int health = maxHealth;

	public Block(Shape shape, BodyImage image) {
		super(world, shape);
		this.image = image;
	}

	public void createBody(int x, int y) {
		addImage(image);
		setPosition(new Vec2(((scaledGridSize * 2) * x) * scaleFactor, ((scaledGridSize * 2) * y) * scaleFactor));
	}

	public void damage(int damage) {
		for (int i = 0; i < damage; i++) {
			damage();
		}
	}

	public void damage() {
		if (!damageable) return;

		health--;

		if (health <= 0) destroy();
		// TODO: Add damage animation.
	}
}