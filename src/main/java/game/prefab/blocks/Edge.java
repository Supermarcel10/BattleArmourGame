package game.prefab.blocks;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Block;
import org.jbox2d.common.Vec2;

import static game.main.Game.*;


public class Edge extends Block {
	private static final String IMAGE = Config.image.get("edge");

	public Edge(int x, int y) {
		super(
				new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor),
				new BodyImage(IMAGE, scaledGridSize * 2 * scaleFactor)
		);

		damageable = false;
		createBody(x, y);
	}
}
