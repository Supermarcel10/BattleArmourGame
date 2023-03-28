package game.prefab;

import game.input.Config;


public enum BlockType {
	BRICK(Config.image.get("wall"), 3),
	EDGE(Config.image.get("edge"), false),
	BASE(Config.image.get("base"), 1, -5000);

	public final String image;
	public int maxHealth = 1;
	public boolean damageable = true;
	public int destroyScore = 40, damageScore = 10;

	BlockType(String image, int maxHealth, int destroyScore) {
		this.image = image;
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
	}

	BlockType(String image, int maxHealth, int destroyScore, int damageScore) {
		this.image = image;
		this.maxHealth = maxHealth;
		this.destroyScore = destroyScore;
		this.damageScore = damageScore;
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
