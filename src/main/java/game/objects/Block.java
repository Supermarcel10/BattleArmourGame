package game.objects;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import static game.main.Game.*;


public class Block extends StaticBody {
	private final BodyImage image;
	protected boolean damageable = true;
	protected int maxHealth = 1;
	public int health = maxHealth;
	public int destroyScore = 40, damageScore = 10;

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
		score += damageScore;

		if (health <= 0) {
			destroy();
			score += destroyScore;
		}
		// TODO: Add damage animation.
	}
}