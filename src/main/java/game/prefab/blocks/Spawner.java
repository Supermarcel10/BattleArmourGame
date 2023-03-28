package game.prefab.blocks;

import city.cs.engine.*;
import game.main.Game;
import game.objects.Enemy;
import game.objects.abstractBody.Body;
import game.prefab.Player;
import game.prefab.TankType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.Arrays;
import java.util.Objects;

import static game.main.Game.enemies;
import static game.main.Game.spawners;


public class Spawner extends Body {
	public Spawner(@NotNull TankType type, Vec2 pos) {
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
			});
		}

		this.destroy();
		spawners.remove(this);
	}
}
