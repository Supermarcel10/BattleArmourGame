package game.prefab.blocks;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Enemy;
import game.objects.Tank;
import game.objects.abstractBody.Body;
import game.prefab.enemies.EnemyType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.HashMap;
import java.util.HashSet;

import static game.main.Game.enemies;
import static java.lang.Thread.sleep;


public class Spawner extends Body {
	// TODO: FIX THIS
	public Spawner(@NotNull EnemyType type, Vec2 pos) {
		super(pos);

		// Create a new ghostly fixture
		new GhostlyFixture(this, new CircleShape(scaleFactor * 2));

		// Change properties
		setFillColor(java.awt.Color.WHITE);
		setGravityScale(0);
		setClipped(true);


		new Thread(() -> {
			try {
				sleep(1000);
				Enemy enemy = type.createEnemy(pos);
				enemy.spawn();
				enemies.add(enemy);
				this.destroy();
			} catch (InterruptedException ignored) {
			}
		}).start();
	}
}
