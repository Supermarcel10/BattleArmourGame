package game.objects;

import city.cs.engine.*;
import game.objects.abstractBody.Body;
import game.prefab.PickupType;
import game.prefab.Player;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import static game.objects.Tank.halfSize;


public class Pickup extends Body implements SensorListener {
	private static final Shape shape = new CircleShape((halfSize / 1.5f) * scaleFactor);
	public final PickupType type;

	public Pickup(PickupType type, Vec2 position) {
		super(0f);
		this.type = type;

		setPosition(position);

		// Create a new ghostly fixture
		new GhostlyFixture(this, shape);
		Sensor sensor = new Sensor(this, shape);

		this.setFillColor(java.awt.Color.RED);

		setBullet(true);

		// Add collision listener.
		sensor.addSensorListener(this);
	}

	public Pickup(PickupType type, int x, int y) {
		this(type, new Vec2(x, y));
	}

	// TODO: Fix this sometimes not registering
	@Override
	public void beginContact(@NotNull SensorEvent e) {
		if (e.getContactBody() instanceof Player p) p.pickUp(this);
	}

	@Override public void endContact(SensorEvent sensorEvent) {}
}
