package character;

public interface Character {
	int health = 100;
	int[] position = new int[2];

	default int getHealth() {
		return health;
	}

	void takeDamage(int damage);

	boolean isDead();

	void attack(Character target);
}
