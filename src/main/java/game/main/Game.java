package game.main;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Block;
import game.objects.Enemy;
import game.prefab.blocks.Base;
import game.prefab.blocks.Brick;
import game.prefab.blocks.Edge;
import game.prefab.Player;
import game.input.Listener;
import game.prefab.enemies.BasicEnemy;
import org.jbox2d.common.Vec2;

import static game.input.Config.resolution;
import static game.main.WindowHandler.view;


public class Game {
	public static World world;
	public static Player player;
	public static float scaleFactor = resolution.x / 1920;
	public static int gridSize = 15;
	public static final int hGridSize = gridSize / 2;
	public static float scaledGridSize;

	public static int score = 0;


	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// TODO: Add saving to the game.
			// TODO: Add any database close statements here.
			// Run garbage collection.
			System.gc();
		}));

		world = new World(Config.fps);

		// Disable the gravity.
		world.setGravity(0);

		// Create the window and main menu.
		WindowHandler.createWindow(world);

		while (WindowHandler.inMenu) {
			// TODO: Make menu handling.

			WindowHandler.inMenu = false;
		}

		loadGame();

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
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
//		thread.start();
	}


	public static void loadGame() {
		scaledGridSize = (((27 * scaleFactor) / gridSize) / scaleFactor);
		Block[][] blocks = new Block[gridSize][gridSize];
		Enemy[] enemies = new Enemy[gridSize * 2];

		// Iterate over the world grid
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				// Create the edges of the world.
				if (i == 0 || i == gridSize - 1 || j == 0 || j == gridSize - 1) {
					blocks[i][j] = new Edge(world, i, j);
				}

				// Make bricks.
				if (i % 2 != 1 && j % 3 != 1) {
					blocks[i][j] = new Brick(world, i - (gridSize / 2), j - (gridSize / 2));
				}

				// Always create a standard base layout.
				if (i == gridSize / 2 && j == 1) {
					// Create a base.
					blocks[i][j] = new Base(world, i - (gridSize / 2), j - (gridSize / 2));
				} else if ((i >= (hGridSize - 1) && i <= (hGridSize + 1)) && (j == 1 || j == 2)) {
					// If no block exists, create a brick border.
					if (blocks[i][j] == null) {
						blocks[i][j] = new Brick(world, i - hGridSize, j - hGridSize);
					}
				}
			}
		}

		// Make a character (with an overlaid image).
		Shape tankShape = new BoxShape(scaledGridSize * scaleFactor * .8f, scaledGridSize * scaleFactor * .8f);
		player = new Player(world, new Vec2(0, 5), tankShape);

		// Make an enemy.
		enemies[0] =  new BasicEnemy(world, new Vec2(5, 5), tankShape);
	}
}
