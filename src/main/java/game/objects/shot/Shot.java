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
import static game.objects.tank.Tank.roundToNearestQuarter;


/**
 * A shot fired by a tank.
 */
public class Shot extends DynamicBody implements SensorListener, StepListener {
	private static final Shape shape = new CircleShape((halfSize * 0.28f));
	protected ShotType type;
	protected List<Tank> penetratedBodies = new ArrayList<>();
	protected int damage;
	protected Tank shooter;

	public Shot(Vec2 position, @NotNull Vec2 travelDirection, @NotNull Tank shooter, float shotSpeed, int damage, ShotType type) {
		super(shooter.shotPollingSpeed);
		setPositionJBox(position);

		moveDirection = travelDirection;
		this.shooter = shooter;
		this.damage = damage;
		this.type = type;

		shots.add(this);

		applyImpulse(moveDirection.mul(shotSpeed * scaleFactor));

		// Create a new ghostly fixture
		new GhostlyFixture(this, shape);
		Sensor sensor = new Sensor(this, shape);

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

	public void destroyForReset() {
		super.destroy();
		world.removeStepListener(this);
	}

	private void checkShotCollided() {
		Iterator<Shot> iterator = shots.iterator();
		while (iterator.hasNext()) {
			Shot shot = iterator.next();
			if (shot == this || shot.shooter == this.shooter) continue;

			if (isWithinDistance(this.getPosition(), shot.getPosition(), (halfSize * 0.28f) )) {
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
//				case EXPLOSIVE -> explode(); // TODO: Fix this.
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
//				case EXPLOSIVE -> explode(); // TODO: Fix this.
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
