package game.objects.block;

import game.IO.Config;


public enum BlockType {
	NONE(), // Used for empty blocks.
	ENEMY_SPAWN("enemySpawn"), // Used for declaring enemy spawn points.
	PLAYER_SPAWN("playerSpawn"), // Used for declaring player spawn points.
	BRICK("wall", 3),
	EDGE("edge", false),
	WATER("water", false, false),
	LEAF("leaf", false, false, true),
	BASE("base", 1, -5000);

	public final String image;

	public int maxHealth = 1;
	public boolean damageable = true;

	public int destroyScore = 40, damageScore = 10;
	public final String destroySound = Config.blockSound.get("damage");
	public final String damageSound = Config.blockSound.get("damage");

	public boolean isSolid = true;
	public boolean isDrivable = false;

	BlockType(String image, boolean damageable, boolean isSolid, boolean isDrivable) {
		this.image = Config.image.get(image);
		this.damageable = damageable;
		this.isSolid = isSolid;
		this.isDrivable = isDrivable;
	}

	BlockType(String image, boolean damageable, boolean isSolid) {
		this.image = Config.image.get(image);
		this.damageable = damageable;
		this.isSolid = isSolid;
	}

	BlockType(String image, int maxHealth, int destroyScore, int damageScore, boolean isSolid, boolean isDrivable) {
		this.image = Config.image.get(image);
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
		this.isSolid = isSolid;
		this.isDrivable = isDrivable;
	}

	BlockType(String image, int maxHealth, int destroyScore, int damageScore, boolean isSolid) {
		this.image = Config.image.get(image);
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
		this.isSolid = isSolid;
	}

	BlockType(String image, int maxHealth, int destroyScore, boolean isSolid) {
		this.image = Config.image.get(image);
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.isSolid = isSolid;
	}

	BlockType(String image, int maxHealth, int destroyScore, int damageScore) {
		this.image = Config.image.get(image);
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
	}

	BlockType(String image, int maxHealth, int destroyScore) {
		this.image = Config.image.get(image);
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
	}

	BlockType(String image, int maxHealth) {
		this.image = Config.image.get(image);
		this.maxHealth = maxHealth;
	}

	BlockType(String image, boolean damageable) {
		this.image = Config.image.get(image);
		this.damageable = damageable;
	}

	BlockType(String image) {
		this.image = Config.image.get(image);
	}

	BlockType() {
		this.image = null;
	}
}
