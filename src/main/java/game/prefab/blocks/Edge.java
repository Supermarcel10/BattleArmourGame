package game.prefab.blocks;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Block;
import org.jbox2d.common.Vec2;

import static game.main.Game.*;


public class Edge extends Block {
	private static final String IMAGE = Config.image.get("edge");

	public Edge(World world, int x, int y) {
		super(
			new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor,
			new Vec2((-24.5f + ((gridSize - 11) * -.1675f)) * scaleFactor, (-24.5f + ((gridSize - 11) * -.1675f)) * scaleFactor)),
			new BodyImage(IMAGE, scaledGridSize * 2 * scaleFactor),
			new Vec2(-25.18f * scaleFactor, -25.18f * scaleFactor)
		);

		createBody(world, x, y);
	}
}
