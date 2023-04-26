package game.objects.block;

import city.cs.engine.*;
import game.objects.abstractBody.StaticBody;
import game.objects.tank.Player;
import game.objects.tank.Tank;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.MainGame.*;


/**
 * A block is a static body that can be placed on the map.
 */
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

		// Check if block is illegal.
		if ((type == BlockType.NONE || type == BlockType.ENEMY_SPAWN || type == BlockType.PLAYER_SPAWN) && !force) {
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

		addImage(image);
		// TODO: Fix this.
		pos = pos.mul((scaledGridSize * 2) * scaleFactor);
		setPositionJBox(pos);
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

	/**
	 * Destroys the block in a given grid Vec2 position.
	 * @param pos The grid Vec2 position of the block to be destroyed.
	 */
	public static void removeIfExists(@NotNull Vec2 pos) {
		if (blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize] != null) {
			blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize].destroy();
			blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize] = null;
		}
	}

	/**
	 * Destroys the block.
	 */
	public void destroy() {
		super.destroy();

		if (type != BlockType.BASE) {
			blocks[(int) getPosition().x + hGridSize][(int) getPosition().y + hGridSize] = null;
		}
	}

	/**
	 * Damages the block.
	 * @param damage The amount of damage to be dealt.
	 * @param shooter The tank that shot the block.
	 */
	public void damage(int damage, Tank shooter) {
		if (!damageable) return;

		if (shooter instanceof Player) score += damage >= health ? damageScore * health : damageScore * damage;
		health -= damage;

		if (health <= 0) {
			destroy();
			if (shooter instanceof Player) {
				score += destroyScore;
				brokenBlocks++;
			}
			soundHandler.play(damageSound);
		} else soundHandler.play(destroySound);

		// TODO: Add damage animation.
	}
}
