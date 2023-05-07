package game.objects.pickup;

import city.cs.engine.*;
import game.objects.abstractBody.DynamicBody;
import game.objects.tank.Player;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static game.MainGame.*;
import static game.objects.tank.Tank.halfSize;


/**
 * Pickup class for pickup perks.
 */
public class Pickup extends DynamicBody implements StepListener {
	private static final int CHECK_RATE = 6;
	private static int untilCheck = 0;

	private static final Shape shape = new CircleShape((halfSize / 1.5f) * scaleFactor);
	public final PickupType type;

	public Pickup(@NotNull PickupType type, Vec2 position) {
		super(0);
		this.type = type;

		setPosition(position);

		// Add to pickups list.
		pickups.add(this);

		// Create a new ghostly fixture
		new GhostlyFixture(this, shape);

		this.addImage(new BodyImage(type.image, halfSize * 2));

		// Add step listener.
		world.addStepListener(this);

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

	@Override
	public void preStep(StepEvent stepEvent) {
		if (untilCheck == 0) {
			for (Player p : player) {
				if (p == null || p.health <= 0) continue;

				Vec2 pos = getPosition();
				Vec2 playerPos = p.getPosition();

				if (isWithinDistance(pos, playerPos, 0.75f)) {
					soundHandler.play(type.soundFile);
					destroy();
					p.pickUp(this);
				}
			}

			untilCheck = CHECK_RATE;
		} else untilCheck--;
	}

	@Override public void postStep(StepEvent stepEvent) {}
}
