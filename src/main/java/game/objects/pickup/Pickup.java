package game.objects.pickup;

import city.cs.engine.*;
import game.objects.abstractBody.DynamicBody;
import game.objects.tank.Player;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static game.MainGame.pickups;
import static game.MainGame.scaleFactor;
import static game.objects.tank.Tank.halfSize;


/**
 * Pickup class for pickup perks.
 */
public class Pickup extends DynamicBody implements SensorListener {
	private static final Shape shape = new CircleShape((halfSize / 1.5f) * scaleFactor);
	public final PickupType type;

	public Pickup(@NotNull PickupType type, Vec2 position) {
		super(0f);
		this.type = type;

		setPosition(position);

		// Add to pickups list.
		pickups.add(this);

		// Create a new ghostly fixture
		new GhostlyFixture(this, shape);
		Sensor sensor = new Sensor(this, shape);

		this.addImage(new BodyImage(type.image, halfSize * 2));

		// Add collision listener.
		sensor.addSensorListener(this);

		// Add a timer to destroy the pickup after 10 seconds.
		new Timer(10000, e -> {
			destroy();

			// Stop after first execution to allow for GC.
			((Timer) e.getSource()).stop();
		}).start();
	}

	/**
	 * Destroy the {@link Pickup} and remove it from the {@link game.MainGame#pickups} list.
	 */
	public void destroy() {
		super.destroy();
		pickups.remove(this);
	}

	/**
	 * Pickup collision listener.
	 * @param e SensorEvent.
	 */
	@Override
	public void beginContact(@NotNull SensorEvent e) {
		// TODO: Fix this sometimes not registering
		if (e.getContactBody() instanceof Player p) {
			p.pickUp(this);
			destroy();
		}
	}

	@Override public void endContact(SensorEvent sensorEvent) {}
}
