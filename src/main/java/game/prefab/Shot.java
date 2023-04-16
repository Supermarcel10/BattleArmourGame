package game.prefab;

import city.cs.engine.*;
import game.objects.Block;
import game.objects.Tank;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static game.main.Game.shots;


public class Shot extends Body implements SensorListener {
	protected int damage;
	protected Tank shooter;

	public Shot(Vec2 position, Vec2 travelDirection, Tank shooter, float speed, int damage) {
		super(speed, position);
		this.shooter = shooter;
		this.speed = speed;
		this.damage = damage;

		spawn(travelDirection);
	}

	private void spawn(Vec2 travelDirection) {
		shots.add(this);

		// Set the body's move direction.
		setMoveDirection(travelDirection);

		// Create a new ghostly fixture
		new GhostlyFixture(this, new CircleShape(0.5f * scaleFactor));
		Sensor sensor = new Sensor(this, new CircleShape(0.5f * scaleFactor));

		// Apply impulse
		applyImpulse(travelDirection.mul(speed));

		// Change properties
		setFillColor(java.awt.Color.RED);
		setGravityScale(0);
		setBullet(true);
		setClipped(true);

		// Add collision listener.
		sensor.addSensorListener(this);

		// Destroy the body after 10 seconds.
		new Timer(10000, e -> {
			this.destroy();
			shots.remove(this);

			// Stop after first execution to allow for GC.
			((Timer) e.getSource()).stop();
		}).start();
	}

	@Override
	public void beginContact(@NotNull SensorEvent sensorEvent) {
		if (sensorEvent.getContactBody() instanceof Block b) {
			b.damage(damage);
			sensorEvent.getSensor().getBody().destroy();

			shots.remove(this);
		}

		if (sensorEvent.getContactBody() instanceof Tank t && t != shooter) {
			t.damage(damage);
			sensorEvent.getSensor().getBody().destroy();

			shots.remove(this);
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
