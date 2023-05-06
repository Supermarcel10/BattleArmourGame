package game.objects.tank;

import city.cs.engine.*;
import game.IO.AM;
import game.MainGame;
import game.objects.abstractBody.DynamicBody;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.Arrays;
import java.util.Objects;

import static game.MainGame.*;
import static game.objects.tank.Tank.halfSize;


/**
 * A {@link Spawn} is a {@link DynamicBody} with a {@link GhostlyFixture} that spawns a {@link Tank} of the specified {@link TankType} at the specified position.
 * <p>
 *     {@link Player}s are spawned immediately, while {@link Enemy}s are spawned after a delay.
 *     After spawning, the {@link Spawn} is destroyed.
 * </p>
 */
public class Spawn extends DynamicBody {
	public Spawn(@NotNull TankType type, Vec2 pos) {
		super(pos);

		// Create a new ghostly fixture
		new GhostlyFixture(this, new CircleShape(halfSize));

		// Change properties
		addImage(new BodyImage(AM.image.get("spawn"), 2 * halfSize));

		if (type == TankType.PLAYER) {
			Player player = type.createPlayer(pos);
			int numOfPlayers = (int) Arrays.stream(MainGame.player).filter(Objects::nonNull).count();
			MainGame.player[numOfPlayers] = player;

			destroy();
		} else {
			spawners.add(this);

			// Spawn enemy after 1 second.
			new Timer(1000, e -> {
				if (!spawners.contains(this)) return;

				enemies.add(type.createEnemy(pos));
				destroy();

				// Stop after first execution to allow for GC.
				((Timer) e.getSource()).stop();
			}).start();
		}
	}

	/**
	 * Destroys the {@link Spawn} and removes it from the {@link MainGame#spawners}.
	 */
	public void destroy() {
		super.destroy();
		spawners.remove(this);
	}
}
