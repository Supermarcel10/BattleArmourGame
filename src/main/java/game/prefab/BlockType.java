package game.prefab;

import game.input.Config;


public enum BlockType {
	BRICK(Config.image.get("wall"), 3),
	EDGE(Config.image.get("edge"), false),
	WATER(Config.image.get("water"), false, false),
	LEAF(Config.image.get("leaf"), false, false, true),
	BASE(Config.image.get("base"), 1, -5000);

	public final String image;

	public int maxHealth = 1;
	public boolean damageable = true;

	public int destroyScore = 40, damageScore = 10;
	public final String destroySound = Config.blockSound.get("damage");
	public final String damageSound = Config.blockSound.get("damage");

	public boolean isSolid = true;
	public boolean isDrivable = false;

	BlockType(String image, boolean damageable, boolean isSolid, boolean isDrivable) {
		this.image = image;
		this.damageable = damageable;
		this.isSolid = isSolid;
		this.isDrivable = isDrivable;
	}

	BlockType(String image, boolean damageable, boolean isSolid) {
		this.image = image;
		this.damageable = damageable;
		this.isSolid = isSolid;
	}

	BlockType(String image, int maxHealth, int destroyScore, int damageScore, boolean isSolid, boolean isDrivable) {
		this.image = image;
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
		this.isSolid = isSolid;
		this.isDrivable = isDrivable;
	}

	BlockType(String image, int maxHealth, int destroyScore, int damageScore, boolean isSolid) {
		this.image = image;
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
		this.isSolid = isSolid;
	}

	BlockType(String image, int maxHealth, int destroyScore, boolean isSolid) {
		this.image = image;
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.isSolid = isSolid;
	}

	BlockType(String image, int maxHealth, int destroyScore, int damageScore) {
		this.image = image;
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
	}

	BlockType(String image, int maxHealth, int destroyScore) {
		this.image = image;
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
	}

	BlockType(String image, int maxHealth) {
		this.image = image;
		this.maxHealth = maxHealth;
	}

	BlockType(String image, boolean damageable) {
		this.image = image;
		this.damageable = damageable;
	}
}
