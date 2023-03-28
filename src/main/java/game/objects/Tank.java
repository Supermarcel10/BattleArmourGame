package game.objects;

import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.SoundClip;
import game.main.Game;
import game.objects.abstractBody.Body;
import game.prefab.Shot;
import org.jbox2d.common.Vec2;

import static game.main.Game.enemies;


public class Tank extends Body {
	private static final Shape shape = new BoxShape(scaledGridSize * scaleFactor * .8f, scaledGridSize * scaleFactor * .8f);

	protected static SoundClip damageSound;
	protected static SoundClip destroySound;

	protected int scoreValue = 0;
	public int health;

	public Tank(Vec2 position) {
		super(position, shape);
	}

	public Tank(float speed, Vec2 position) {
		super(speed, position, shape);
	}

	public void shoot() {
		// Get the tank direction based on the angle of it.
		// TODO: Limit shooting speed.

		Vec2 moveDirection = new Vec2(
			(float) -Math.round(Math.sin(Math.round(this.getAngle() / (Math.PI / 2)) * (Math.PI / 2))),
			(float) Math.round(Math.cos(Math.round(this.getAngle() / (Math.PI / 2)) * (Math.PI / 2)))
		);

		new Shot(this.getPosition(), moveDirection, this);
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
			Game.score += scoreValue;
			destroy();
			enemies.remove(this);

			// Play the destroy sound if it exists.
			if (destroySound != null) {
				destroySound.play();
			}
		} else if (damageSound != null) {
			damageSound.play();
		}
	}

	public void setMaxHealth(int maxHealth) {
		this.health = maxHealth;
	}
}
