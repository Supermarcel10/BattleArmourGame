package game.objects.tank;

import city.cs.engine.*;
import game.MainGame;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.Arrays;
import java.util.Objects;

import static game.MainGame.*;
import static game.objects.tank.Tank.halfSize;


public class Spawn extends Body {
	public Spawn(@NotNull TankType type, Vec2 pos) {
		super(pos);

		// Create a new ghostly fixture
		new GhostlyFixture(this, new CircleShape(halfSize));
		// FIXME:
		//  Exception in thread "Thread-0" java.lang.NullPointerException: Cannot invoke "org.jbox2d.dynamics.Fixture.setUserData(Object)" because "this.b2fixture" is null
		//	at city.cs.engine.Fixture.<init>(Fixture.java:51)
		//	at city.cs.engine.GhostlyFixture.<init>(GhostlyFixture.java:42)
		//	at city.cs.engine.GhostlyFixture.<init>(GhostlyFixture.java:32)
		//	at game.objects.tank.Spawn.<init>(Spawn.java:23)
		//	at game.MainGame.enemySpawn(MainGame.java:194)
		//	at java.base/java.lang.Thread.run(Thread.java:1589)


		// Change properties
		setFillColor(java.awt.Color.WHITE);

		if (type == TankType.PLAYER) {
			Player player = type.createPlayer(pos);
			int numOfPlayers = (int) Arrays.stream(MainGame.player).filter(Objects::nonNull).count();
			MainGame.player[numOfPlayers] = player;

			removeSpawner();
		} else {
			spawners.add(this);

			// Spawn enemy after 1 second.
			new Timer(1000, e -> {
				if (!spawners.contains(this)) return;

				enemies.add(type.createEnemy(pos));
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
