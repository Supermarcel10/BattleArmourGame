//package character.enemies;
//
//import character.Character;
//import character.Enemy;
//
//public class Goblin implements Enemy {
//	private int health;
//	private int attackPower;
//
//	public Goblin(int health, int attackPower) {
//		this.health = health;
//		this.attackPower = attackPower;
//	}
//
//	public int getHealth() {
//		return health;
//	}
//
//	public void takeDamage(int damage) {
//		health -= damage;
//	}
//
//	public boolean isDead() {
//		return health <= 0;
//	}
//
//	@Override
//	public void attack(Character target) {
//
//	}
//
//	public int getAttackPower() {
//		return attackPower;
//	}
//
//	public void setAttackPower(int attackPower) {
//		this.attackPower = attackPower;
//	}
//}
