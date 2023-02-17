//package character.enemies;
//
//import character.Character;
//import character.Enemy;
//
//public class Ogre implements Enemy {
//	private int attackPower;
//
//	public Ogre(int health, int attackPower) {
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
//	@Override
//	public boolean isDead() {
//		System.out.println("Ogre has died!");
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
