package game.objects.tank;

import city.cs.engine.*;
import game.Game;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.Arrays;
import java.util.Objects;

import static game.Game.*;
import static game.objects.tank.Tank.halfSize;


public class Spawn extends Body {
	public Spawn(@NotNull TankType type, Vec2 pos) {
		super(pos);

		// Create a new ghostly fixture
		new GhostlyFixture(this, new CircleShape(halfSize));

		// Change properties
		setFillColor(java.awt.Color.WHITE);

		if (type == TankType.PLAYER) {
			Player player = type.createPlayer(pos);
			int numOfPlayers = (int) Arrays.stream(Game.player).filter(Objects::nonNull).count();
			Game.player[numOfPlayers] = player;

			removeSpawner();
		} else {
			spawners.add(this);

			// Spawn enemy after 1 second.
			new Timer(1000, e -> {
				Enemy enemy = type.createEnemy(pos);
				enemies.add(enemy);

				removeSpawner();

				// Stop after first execution to allow for GC.
				((Timer) e.getSource()).stop();
			}).start();
		}
	}

	private void removeSpawner() {
		this.destroy();
		spawners.remove(this);
	}
}
