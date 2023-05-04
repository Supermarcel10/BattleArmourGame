package game.objects.block;

import game.IO.AM;
import org.jetbrains.annotations.NotNull;


/**
 * Enum for block types.
 * This enum is used to declare the type of block and its properties.
 */
public enum BlockType {
	NONE(), // Used for empty blocks.
	ENEMY_SPAWN(), // Used for declaring enemy spawn points.
	PLAYER_SPAWN(), // Used for declaring player spawn points.
	BRICK( 3),
	EDGE(false),
	WATER(false, false),
	LEAF(false, false, true),
	BASE(1, -5000);

	public final String image;

	public int maxHealth = 1;
	public boolean damageable = true;

	public int destroyScore = 40, damageScore = 10;
	public final String destroySound = AM.blockSound.get("brickBreak");
	public final String damageSound = AM.blockSound.get("brickBreak");

	public boolean isSolid = true;
	public boolean isDrivable = false;

	BlockType(boolean damageable, boolean isSolid, boolean isDrivable) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.damageable = damageable;
		this.isSolid = isSolid;
		this.isDrivable = isDrivable;
	}

	BlockType(boolean damageable, boolean isSolid) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.damageable = damageable;
		this.isSolid = isSolid;
	}

	BlockType(int maxHealth, int destroyScore, int damageScore, boolean isSolid, boolean isDrivable) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
		this.isSolid = isSolid;
		this.isDrivable = isDrivable;
	}

	BlockType(int maxHealth, int destroyScore, int damageScore, boolean isSolid) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
		this.isSolid = isSolid;
	}

	BlockType(int maxHealth, int destroyScore, boolean isSolid) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.isSolid = isSolid;
	}

	BlockType(int maxHealth, int destroyScore, int damageScore) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
	}

	BlockType(int maxHealth, int destroyScore) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
	}

	BlockType(int maxHealth) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.maxHealth = maxHealth;
	}

	BlockType(boolean damageable) {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.damageable = damageable;
	}

	BlockType() {
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.damageable = false;
	}

	/**
	 * Converts the enum name to camel case.
	 * @param s The enum name.
	 * @return The enum name in camel case.
	 */
	private static @NotNull String toCamelCase(@NotNull String s) {
		String[] parts = s.split("_");
		StringBuilder out = new StringBuilder();
		out.append(parts[0].toLowerCase());

		for (int i = 1; i < parts.length; i++) {
			out.append(parts[i].substring(0, 1).toUpperCase());
			out.append(parts[i].substring(1).toLowerCase());
		}

		return out.toString();
	}
}
