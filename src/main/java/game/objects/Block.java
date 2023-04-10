package game.objects;

import city.cs.engine.*;
import game.prefab.BlockType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.main.Game.*;


public class Block extends StaticBody {
	private static final BoxShape shape = new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor);
	private int destroyScore, damageScore;
	private static BodyImage image;

	// TODO: Consider putting in ENUM
	protected static SoundClip damageSound;
	protected static SoundClip destroySound;

	protected boolean damageable;
	protected int maxHealth;
	public int health;

	public Block(@NotNull BlockType type, @NotNull Vec2 pos) {
		super(world, shape);
		image = new BodyImage(type.image, scaledGridSize * 2 * scaleFactor);
		destroyScore = type.destroyScore;
		damageScore = type.damageScore;
		health = maxHealth = type.maxHealth;
		damageable = type.damageable;

		createBody(pos);
	}

	public Block(@NotNull BlockType type, int x, int y) {
		this(type, new Vec2(x, y));
	}

	public void createBody(@NotNull Vec2 position) {
		addImage(image);
		position = position.mul((scaledGridSize * 2) * scaleFactor);
		setPosition(position);
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