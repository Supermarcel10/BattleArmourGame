package game.prefab;

import city.cs.engine.*;
import game.objects.Block;
import game.objects.Tank;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static game.main.Game.shots;
import static game.objects.Tank.halfSize;


public class Shot extends Body implements SensorListener {
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

		// Destroy the body after 10 seconds.
		new Timer(10000, e -> {
			destroyShot();

			// Stop after first execution to allow for GC.
			((Timer) e.getSource()).stop();
		}).start();
	}

	private void destroyShot() {
		destroy();
		shots.remove(this);
	}

	@Override
	public void beginContact(@NotNull SensorEvent sensorEvent) {
		if (sensorEvent.getContactBody() instanceof Block b && b.type.isSolid) {
			switch (type) {
				case BASIC, PENETRATING -> b.damage(damage);
				case EXPLOSIVE -> explode();
			}

			destroyShot();
		}

		if (sensorEvent.getContactBody() instanceof Tank t && t != shooter) {
			switch (type) {
				case BASIC -> {
					t.damage(damage);
					destroyShot();
				}
				case PENETRATING -> {
					if (!penetratedBodies.contains(t)) {
						t.damage(damage);
						penetratedBodies.add((Tank) sensorEvent.getContactBody());

						if (penetratedBodies.size() >= type.numberOfPenetrations) destroyShot();
					}
				}
				case EXPLOSIVE -> explode();
			}
		}

		// TODO: BUG: Fix this, where the shot cannot find other shots.
//		if (sensorEvent.getContactBody() instanceof Shot s && s != this) {
//			s.destroy();
//			sensorEvent.getSensor().getBody().destroy();
//
//			shots.remove(this);
//		}
	}

	@Override public void endContact(SensorEvent sensorEvent) {}
}
