package game.prefab;

import city.cs.engine.*;
import game.main.Game;
import game.objects.Enemy;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.Arrays;
import java.util.Objects;

import static game.main.Game.enemies;
import static game.main.Game.spawners;


public class Spawn extends Body {
	public Spawn(@NotNull TankType type, Vec2 pos) {
		super(pos);

		spawners.add(this);

		// Create a new ghostly fixture
		new GhostlyFixture(this, new CircleShape(scaleFactor * 2));

		// Change properties
		setFillColor(java.awt.Color.WHITE);
		setGravityScale(0);
		setClipped(true);

		if (type == TankType.PLAYER) {
			Player player = type.createPlayer(pos);
			int numOfPlayers = (int) Arrays.stream(Game.player).filter(Objects::nonNull).count();
			Game.player[numOfPlayers] = player;
		} else {
			// Spawn enemy after 1 second.
			new Timer(1000, e -> {
				Enemy enemy = type.createEnemy(pos);
				enemy.spawn();
				enemies.add(enemy);

				// Stop after first execution to allow for GC.
				((Timer) e.getSource()).stop();
			}).start();
		}

		this.destroy();
		spawners.remove(this);
	}

	public Spawn(@NotNull TankType type, float x, float y) {
		this(type, new Vec2(x, y));
	}
}
