package game.prefab.blocks;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.World;
import game.input.Config;
import game.objects.Block;

import static game.main.Game.scaleFactor;
import static game.main.Game.scaledGridSize;


public class Brick extends Block {
	private static final String IMAGE = Config.image.get("wall");

	public Brick(int x, int y) {
		super(
			new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor),
			new BodyImage(IMAGE, scaledGridSize * 2 * scaleFactor)
		);

		damageable = true;
		maxHealth = 3;
		health = maxHealth;

		createBody(x, y);
	}
}
