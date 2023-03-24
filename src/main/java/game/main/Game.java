package game.main;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.input.Config;
import game.objects.Block;
import game.objects.Enemy;
import game.prefab.blocks.Base;
import game.prefab.blocks.Brick;
import game.prefab.blocks.Edge;
import game.prefab.Player;
import game.input.Listener;
import game.prefab.enemies.BasicEnemy;
import game.prefab.enemies.ExplodingEnemy;
import game.prefab.enemies.FastEnemy;
import game.prefab.enemies.HeavyEnemy;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.util.HashSet;

import static game.input.Config.resolution;
import static game.main.WindowHandler.view;
import static java.lang.Thread.sleep;


public class Game {
	public static World world;
	public static Player[] player = new Player[2];
	public static Block[][] blocks;
	public static HashSet<Enemy> enemies = new HashSet<>();

	public static Vec2 basePos;

	public static float scaleFactor = resolution.x / 1920;
	public static int gridSize = 15;
	public static final int hGridSize = gridSize / 2;
	public static float scaledGridSize;

	public static int score = 0, postUpdateScore = 0;


	public static void main(String[] args) throws InterruptedException {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// TODO: Add saving to the game.
			// TODO: Add any database close statements here.
			// Run garbage collection.
			System.gc();
		}));

		world = new World(Config.fps);

		// Create the window and main menu.
		WindowHandler.createWindow(world);

		while (WindowHandler.inMenu) {
			// TODO: Make menu handling.
			WindowHandler.inMenu = false;
		}

		// Disable the gravity and change background color.
		world.setGravity(0);
		view.setBackground(Color.decode("#fcf8de"));

		loadGame();
		WindowHandler.createGameOverlay();

		// Add Keyboard & Mouse listeners.
		Listener listener = new Listener();
		view.addMouseListener(listener);
		view.addKeyListener(listener);
		world.addStepListener(listener);

		// Start world simulation
		world.start();

		// Create a thread to print the FPS.
		Thread thread = new Thread(() -> {
			while (true) {
				System.out.println(world.getSimulationSettings().getAveragedFPS());
				// Sleep for 1 second.
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
//		thread.start();
	}

	public static void loadGame() {
		scaledGridSize = (((27 * scaleFactor) / gridSize) / scaleFactor);
		blocks = new Block[gridSize][gridSize];

		// Iterate over the world grid
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				// Create the edges of the world.
				if (i == 0 || i == gridSize - 1 || j == 0 || j == gridSize - 1) {
					blocks[i][j] = new Edge((int) scaledGridSize * (-7 + i), (int) scaledGridSize * (-7 + j));
				}

				// Make bricks.
				if (!(i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1) && i % 2 != 1 && j % 3 != 1) {
					blocks[i][j] = new Brick(i - hGridSize, j - hGridSize);
				}

				// Always create a standard base layout.
				if (i == gridSize / 2 && j == 1) {
					// Create a base.
					basePos = new Vec2(i, j);
					blocks[i][j] = new Base(i - (gridSize / 2), j - (gridSize / 2));
				} else if ((i >= (hGridSize - 1) && i <= (hGridSize + 1)) && (j == 1 || j == 2)) {
					//If no block exists, create a brick border.
	                if (i == 0 || i == gridSize - 1 || j == 0 || j == gridSize - 1) {
						blocks[i][j] = new Edge(i, j);
					}

					if (blocks[i][j] == null) {
						blocks[i][j] = new Brick(i - hGridSize, j - hGridSize);
					}
				}
			}
		}

		// Make a character (with an overlaid image).
		Shape tankShape = new BoxShape(scaledGridSize * scaleFactor * .8f, scaledGridSize * scaleFactor * .8f);
		player[0] = new Player(world, new Vec2(-1, 0), tankShape);
		player[1] = new Player(world, new Vec2(1, 0), tankShape);

		// Make a few enemies for testing.
		enemies.add(new ExplodingEnemy(world, new Vec2(-6, 6), tankShape));
		enemies.add(new BasicEnemy(world, new Vec2(-2, 6), tankShape));
		enemies.add(new HeavyEnemy(world, new Vec2(2, 6), tankShape));
		enemies.add(new FastEnemy(world, new Vec2(6, 6), tankShape));

		SoundHandler.playBackgroundMusic();

		// Spawn enemies progressively.
		Thread proceduralSpawnEnemy = new Thread(() -> {
			while (true) {
				if (enemies.size() < 10) {
					enemies.add(new BasicEnemy(new Vec2((int) (Math.random() * 12) - 6, 6), tankShape));
				}

				try {
					sleep(5000 + (int) (Math.random() * 5000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
//		proceduralSpawnEnemy.start();
	}
}
