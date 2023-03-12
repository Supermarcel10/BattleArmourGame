package game.objects;

import city.cs.engine.World;
import game.main.Game;
import game.objects.abstractBody.Body;
import game.prefab.Shot;
import org.jbox2d.common.Vec2;

import static game.main.Game.score;


public class Tank extends Body {
	protected int scoreValue = 0;
	public int health;

	public Tank(World world, Vec2 position) {
		super(world, position);
	}

	public Tank(float speed, World world, Vec2 position) {
		super(speed, world, position);
	}

	public void shoot() {
		// Get the player direction based on the angle of the tank.
		Vec2 moveDirection = new Vec2(
			(float) Math.round(Math.cos(Math.round(body.getAngle() / (Math.PI / 2)) * (Math.PI / 2))),
			(float) Math.round(Math.sin(Math.round(body.getAngle() / (Math.PI / 2)) * (Math.PI / 2)))
		);

		new Shot(speed, world, body.getPosition(), moveDirection, this);
	}

	public void damage(int damage) {
		for (int i = 0; i < damage; i++) {
			damage();
		}
	}

	public void damage() {
		health--;
		// TODO: Add explosion effect.
		if (health == 0) {
			System.out.println("Enemy destroyed!");
			score += scoreValue;
			destroy();
		}
	}

	public void setMaxHealth(int maxHealth) {
		this.health = maxHealth;
	}
}
