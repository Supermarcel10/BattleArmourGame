package game.objects;

import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.SoundClip;
import game.objects.abstractBody.Body;
import game.prefab.Shot;
import org.jbox2d.common.Vec2;

import javax.swing.*;

import static game.main.Game.*;


public class Tank extends Body {
	protected static float halfSize = scaledGridSize * scaleFactor * .8f;
	private static final Shape shape = new BoxShape(halfSize, halfSize);

	private boolean canShoot = true;
	private final int shootingDelay = 500;
	private final Timer shootingTimer = new Timer(shootingDelay, e -> canShoot = true);

	protected static SoundClip damageSound;
	protected static SoundClip destroySound;

	protected int scoreValue;
	public int health;

	public Tank(Vec2 position) {
		super(position, shape);
		shootingTimer.setRepeats(false);
	}

	public Tank(float speed, Vec2 position) {
		super(speed, position, shape);
		shootingTimer.setRepeats(false);
	}

	public void shoot() {
		// Limit the tank shooting speed.
		if (!canShoot) {
			return;
		}

		canShoot = false;
		shootingTimer.restart();

		// Get the tank direction based on the angle of it.
		Vec2 moveDirection = new Vec2(
			(float) -Math.round(Math.sin(Math.round(this.getAngle() / (Math.PI / 2)) * (Math.PI / 2))),
			(float) Math.round(Math.cos(Math.round(this.getAngle() / (Math.PI / 2)) * (Math.PI / 2)))
		);

		new Shot(this.getPosition(), moveDirection, this);
	}

	public void damage() {
		health--;

		// TODO: Add explosion effect.
		if (health == 0) {
			score += scoreValue;
			kills++;

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

	protected void changeShootingDelay(int delay) {
		shootingTimer.setDelay(delay);
		shootingTimer.setInitialDelay(delay);
	}

	public void setMaxHealth(int maxHealth) {
		this.health = maxHealth;
	}
}
