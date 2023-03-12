package game.objects;

import city.cs.engine.World;
import game.objects.abstractBody.Body;
import game.prefab.Shot;
import org.jbox2d.common.Vec2;


public class Tank extends Body {
	private int maxHealth;
	public int health;

	public Tank(World world, Vec2 position) {
		super(world, position);
	}

	public Tank(float speed, World world, Vec2 position) {
		super(speed, world, position);
	}

	public void spawn() {
		position = new Vec2(0 * scaleFactor,0 * scaleFactor);
		speed = 0.4f;
		super.spawn();
	}

	public void shoot() {
		// TODO: FIX SHOOTING!
		new Shot(speed, world, body.getPosition(), moveDirection);
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
}
