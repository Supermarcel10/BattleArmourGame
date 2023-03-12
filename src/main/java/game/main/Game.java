package game.main;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Block;
import game.prefab.Brick;
import game.prefab.Edge;
import game.prefab.Player;
import game.input.Listener;
import org.jbox2d.common.Vec2;

import static game.input.Config.resolution;
import static game.main.WindowHandler.view;


public class Game {
	public static World world;
	public static Player player;
	public static float scaleFactor = resolution.x / 1920;
	public static int gridSize = 15;
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
		MainMenu.createMenu(WindowHandler.frame);

		while (MainMenu.inMenu) {
			// TODO: Make menu handling.

			MainMenu.inMenu = false;
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
		scaledGridSize = (float) (((27 * scaleFactor) / gridSize) / scaleFactor);
		Block[][] blocks = new Block[gridSize][gridSize];

		// Create the edges of the world.
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (i == 0 || i == gridSize - 1 || j == 0 || j == gridSize - 1) {
					// Create a static body for the cell
					new Edge(world, i, j);
				}
			}
		}

		// Make an example array of bricks.
		for (int i = 1; i < gridSize - 1; i++) {
			for (int j = 1; j < gridSize - 1; j++) {
				if (i % 2 != 1 && j % 3 != 1) {
					blocks[i][j] = new Brick(world, i - (gridSize / 2), j - (gridSize / 2));
				}
			}
		}

		// Make a character (with an overlaid image).
		Shape playerShape = new BoxShape(scaledGridSize * scaleFactor * .8f, scaledGridSize * scaleFactor * .8f);
		player = new Player(world, new Vec2(0, 0), playerShape);

		System.out.println("Game loaded.");

//		StaticBody brick = new StaticBody(world, brickShape);
//		brick.addImage(brickImage);
	}
}
