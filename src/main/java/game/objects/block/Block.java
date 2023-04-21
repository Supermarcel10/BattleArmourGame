package game.objects.block;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.Game.*;


public class Block extends StaticBody {
	private static final BoxShape shape = new BoxShape(scaledGridSize * scaleFactor, scaledGridSize * scaleFactor);
	private final int damageScore, destroyScore;
	private static BodyImage image;

	public final BlockType type;

	protected static String damageSound, destroySound;

	protected boolean damageable;
	protected int maxHealth;
	public int health;

	public Block(@NotNull BlockType type, @NotNull Vec2 pos, boolean force) throws IllegalStateException {
		super(world, shape);

		// Check is not none.
		if (type == BlockType.NONE || type == BlockType.ENEMY_SPAWN || type == BlockType.PLAYER_SPAWN) {
			throw new IllegalStateException("Block type cannot be " + type.name() + "!");
		}

		// Check if no more than 1 base exists.
		if (type == BlockType.BASE && !force) {
			if (basePos == null) basePos = new Vec2(pos.x + hGridSize, pos.y + hGridSize);
			else throw new IllegalStateException("Base already exists!");
		}

		if (blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize] != null) {
			if (force) removeIfExists(pos);
			else throw new IllegalStateException("Block already occupied!");
		}

		this.type = type;
		image = new BodyImage(type.image, scaledGridSize * 2 * scaleFactor);

		destroyScore = type.destroyScore;
		damageScore = type.damageScore;
		health = maxHealth = type.maxHealth;
		damageable = type.damageable;

		damageSound = type.damageSound;
		destroySound = type.destroySound;

		blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize] = this;

		if (type.isDrivable) this.getFixtureList().get(0).destroy();

		createBody(pos);
	}

	public Block(@NotNull BlockType type, @NotNull Vec2 pos) throws IllegalStateException {
		this(type, pos, false);
	}

	public Block(@NotNull BlockType type, int x, int y, boolean force) throws IllegalStateException {
		this(type, new Vec2(x, y), force);
	}

	public Block(@NotNull BlockType type, int x, int y) throws IllegalStateException {
		this(type, new Vec2(x, y));
	}

	private void createBody(@NotNull Vec2 position) {
		addImage(image);
		// TODO: Fix this.
		position = position.mul((scaledGridSize * 2) * scaleFactor);
		setPosition(position);
	}

	public static void removeIfExists(@NotNull Vec2 pos) {
		if (blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize] != null) {
			blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize].destroy();
			blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize] = null;
		}
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
		} else soundHandler.play(destroySound);

		// TODO: Add damage animation.
	}
}
