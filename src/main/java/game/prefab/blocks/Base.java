package game.prefab.blocks;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.World;
import game.input.Config;
import game.objects.Block;

import static game.main.Game.scaleFactor;
import static game.main.Game.scaledGridSize;


public class Base extends Block {
	private static final String IMAGE = Config.image.get("base");

	public Base(World world, int x, int y) {
		super(
				new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor),
				new BodyImage(IMAGE, scaledGridSize * 2 * scaleFactor)
		);

		maxHealth = 1;
		createBody(world, x, y);
	}
}
