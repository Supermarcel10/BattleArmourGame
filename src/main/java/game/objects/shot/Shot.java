package game.objects.shot;

import city.cs.engine.*;
import game.objects.block.Block;
import game.objects.tank.Tank;
import game.objects.abstractBody.DynamicBody;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static game.MainGame.*;
import static game.objects.tank.Tank.halfSize;


/**
 * A shot fired by a tank.
 */
public class Shot extends DynamicBody implements SensorListener, StepListener {
	private static final Shape shape = new CircleShape((halfSize * 0.28f) * scaleFactor);
	protected ShotType type;
	protected List<Tank> penetratedBodies = new ArrayList<>();
	protected int damage;
	protected Tank shooter;

	public Shot(Vec2 position, @NotNull Vec2 travelDirection, Tank shooter, float speed, int damage, ShotType type) {
		super(speed);
		setPositionJBox(position);

		this.shooter = shooter;
		this.speed = speed;
		this.damage = damage;
		this.type = type;

		shots.add(this);

		// Create a new ghostly fixture
		new GhostlyFixture(this, shape);
		Sensor sensor = new Sensor(this, shape);

		// Apply impulse
		// TODO: Fix resolution affecting the speed of the shot.
		// TODO: Fix frame time affecting the speed of the shot.
		applyImpulse(travelDirection.mul(speed));

		// Change properties
		setFillColor(java.awt.Color.RED);
		setBullet(true);

		// Add collision listener.
		sensor.addSensorListener(this);
		world.addStepListener(this);

		// Destroy the body after 10 seconds.
		new Timer(10000, e -> {
			destroy();

			// Stop after first execution to allow for GC.
			((Timer) e.getSource()).stop();
		}).start();
	}

	/**
	 * Destroy the {@link Shot} and remove it from the {@link game.MainGame#shots} list.
	 */
	public void destroy() {
		super.destroy();
		world.removeStepListener(this);
		shots.remove(this);
	}

	private void checkShotCollided() {
		Iterator<Shot> iterator = shots.iterator();
		while (iterator.hasNext()) {
			Shot shot = iterator.next();
			if (shot == this) continue;

			if (isWithinDistance(this.getPosition(), shot.getPosition(), ((halfSize * 0.28f) * scaleFactor) / 2f)) {
				iterator.remove();
				shot.destroy();
				this.destroy();
				break;
			}
		}
	}

	/**
	 * Handle the collision event between Tanks and Blocks as well as other shots.
	 * @param sensorEvent The sensor event.
	 */
	@Override
	public void beginContact(@NotNull SensorEvent sensorEvent) {
		if (sensorEvent.getContactBody() instanceof Block b && b.type.isSolid) {
			switch (type) {
				case BASIC, PENETRATING -> b.damage(damage, shooter);
				case EXPLOSIVE -> explode();
			}

			destroy();
		}

		if (sensorEvent.getContactBody() instanceof Tank t && t != shooter) {
			switch (type) {
				case BASIC -> {
					t.damage(damage, shooter);
					destroy();
				}
				case PENETRATING -> {
					if (!penetratedBodies.contains(t)) {
						t.damage(damage, shooter);
						penetratedBodies.add((Tank) sensorEvent.getContactBody());

						if (penetratedBodies.size() >= type.numberOfPenetrations) destroy();
					}
				}
				case EXPLOSIVE -> explode();
			}
		}
	}

	@Override
	public void preStep(StepEvent stepEvent) {
			checkShotCollided();
	}

	@Override public void endContact(SensorEvent sensorEvent) {}
	@Override public void postStep(StepEvent stepEvent) {}
}
