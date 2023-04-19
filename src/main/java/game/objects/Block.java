package game.objects;

import city.cs.engine.*;
import game.prefab.BlockType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.main.Game.*;


public class Block extends StaticBody {
	private static final BoxShape shape = new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor);
	private final int damageScore, destroyScore;
	private static BodyImage image;

	public final BlockType type;

	protected static String damageSound, destroySound;

	protected boolean damageable;
	protected int maxHealth;
	public int health;

	public Block(@NotNull BlockType type, @NotNull Vec2 pos) throws IllegalStateException {
		super(world, shape);

		image = new BodyImage(type.image, scaledGridSize * 2 * scaleFactor);

		this.type = type;

		destroyScore = type.destroyScore;
		damageScore = type.damageScore;
		health = maxHealth = type.maxHealth;
		damageable = type.damageable;

		damageSound = type.damageSound;
		destroySound = type.destroySound;

		if (type == BlockType.BASE) {
			if (basePos == null) {
				basePos = new Vec2(pos.x + hGridSize, pos.y + hGridSize);
			}
			else throw new IllegalStateException("Base already exists!");
		}

		createBody(pos);
	}

	public Block(@NotNull BlockType type, int x, int y) throws IllegalStateException {
		this(type, new Vec2(x, y));
	}

	public void createBody(@NotNull Vec2 position) {
		addImage(image);
		position = position.mul((scaledGridSize * 2) * scaleFactor);
		setPosition(position);
	}

	public void damage(int damage) {
		if (!damageable) return;

		score += damage >= health ? damageScore * health : damageScore * damage;
		health -= damage;

		if (health <= 0) {
			destroy();
			score += destroyScore;
			brokenBlocks++;
			soundHandler.play(damageSound);
		} else soundHandler.play(destroySound);;

		// TODO: Add damage animation.
	}
}
