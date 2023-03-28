package game.prefab;

import city.cs.engine.*;
import game.main.Game;
import game.objects.Block;
import game.objects.Tank;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static game.main.Game.shots;


public class Shot extends Body implements SensorListener {
	private static final float speed = 100f;
	protected Tank shooter;

	public Shot(Vec2 position, Vec2 travelDirection, Tank shooter) {
		super(speed, position);
		this.shooter = shooter;
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
		}).start();
	}

	@Override
	public void beginContact(@NotNull SensorEvent sensorEvent) {
		if (sensorEvent.getContactBody() instanceof Block b) {
			b.damage();
			sensorEvent.getSensor().getBody().destroy();

			shots.remove(this);
		}

		if (sensorEvent.getContactBody() instanceof Tank t && t != shooter) {
			t.damage();
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
