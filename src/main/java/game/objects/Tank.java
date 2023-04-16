package game.objects;

import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.SoundClip;
import game.objects.abstractBody.Body;
import game.prefab.Shot;
import game.prefab.ShotType;
import org.jbox2d.common.Vec2;

import javax.swing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static game.main.Game.*;


public class Tank extends Body {
	protected static float halfSize = scaledGridSize * scaleFactor * .8f;
	private static final Shape shape = new BoxShape(halfSize, halfSize);

	private boolean canShoot = true;
	private final int shootingDelay = 500;
	protected float shotSpeed = 150f;
	protected int shotDamage = 1;
	protected int quadShotRemaining = 0;
	protected List<HashMap<ShotType, Integer>> availableShots = new ArrayList<>();
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

		// Find the most powerful shot type available.
		AtomicReference<ShotType> shotType = new AtomicReference<>(ShotType.BASIC);

		if (availableShots != null) {
			for (HashMap<ShotType, Integer> shot : availableShots) {
				shot.keySet().forEach(key -> {
					if (shotType.get().ordinal() < key.ordinal()) shotType.set(key);
				});
			}

			// Reduce the amount of the shot type available.
			availableShots.forEach(shot -> {
				if (shot.containsKey(shotType.get())) {
					shot.put(shotType.get(), shot.get(shotType.get()) - 1);

					// If the shot type is out of ammo, remove it.
					if (shot.get(shotType.get()) <= 0) shot.remove(shotType.get());
				}
			});
		}

		// If the tank is in quad shot mode, create 4 shots instead of 1.
		if (quadShotRemaining > 0) {
			for (int i = 0; i < 4; i++) {
				new Shot(this.getPosition(), new Vec2((float) Math.cos(i * Math.PI / 2), (float) Math.sin(i * Math.PI / 2)), this, shotSpeed, shotDamage, shotType.get());
			}

			quadShotRemaining--;
		} else {
			// Get the tank direction based on the angle of it.
			Vec2 moveDirection = new Vec2(
				(float) -Math.round(Math.sin(Math.round(this.getAngle() / (Math.PI / 2)) * (Math.PI / 2))),
				(float) Math.round(Math.cos(Math.round(this.getAngle() / (Math.PI / 2)) * (Math.PI / 2)))
			);

			// Create the shot.
			new Shot(this.getPosition(), moveDirection, this, shotSpeed, shotDamage, shotType.get());
		}
	}

	public void damage(int damage) {
		health -= damage;

		// TODO: Add explosion effect.
		if (health <= 0) {
			score += scoreValue;
			kills++;

			destroy();
			//noinspection SuspiciousMethodCalls
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
