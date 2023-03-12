package game.prefab;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.World;
import game.input.Config;
import game.objects.Block;

import static game.main.Game.scaleFactor;
import static game.main.Game.scaledGridSize;


public class Brick extends Block {
	private static final String IMAGE = Config.image.get("wall");
	private final int maxHealth = 3;
	private int health = maxHealth;

	public Brick(World world, int x, int y) {
		super(
			new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor),
			new BodyImage(IMAGE, scaledGridSize * 2 * scaleFactor)
		);

		createBody(world, x, y);
	}

	public Brick damage(int damage) {
		for (int i = 0; i < damage; i++) {
			damage();
		}
		return this;
	}

	public Brick damage() {
		health--;

		if (health <= 0) {
			body.destroy();
		}
		// TODO: Add damage animation.
		return this;
	}
}
