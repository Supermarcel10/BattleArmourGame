package character;

public class Player implements Character {
	private int health;

	public Player(int health) {
		this.health = health;
	}

	public int getHealth() {
		return health;
	}

	public void takeDamage(int damage) {
		health -= damage;
	}

	public boolean isDead() {
		return health <= 0;
	}

	public void attack(Character target) {
		// Perform attack on target
	}
}
