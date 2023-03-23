package game.prefab;

import city.cs.engine.*;
import game.objects.Block;
import game.objects.Tank;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;

import javax.swing.*;


public class Shot extends Body implements SensorListener {
	private static final float speed = 100f;
	protected Tank shooter;
	private final Vec2 travelDirection;

	public Shot(float speed, World world, Vec2 position, Vec2 travelDirection, Tank shooter) {
		super(speed, world, position);
		this.shooter = shooter;
		this.travelDirection = travelDirection;
		spawn();
	}

//	@Override
	public void spawn() {
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
		}).start();
	}

	@Override
	public void beginContact(SensorEvent sensorEvent) {
		System.out.println(sensorEvent.getContactBody().getClass());
		if (sensorEvent.getContactBody() instanceof Block b) {
			System.out.println("Hit block");
			b.damage();
			sensorEvent.getSensor().getBody().destroy();
		}
	}

	@Override public void endContact(SensorEvent sensorEvent) {}
}
