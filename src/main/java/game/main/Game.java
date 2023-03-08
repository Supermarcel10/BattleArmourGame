package game.main;

import city.cs.engine.BoxShape;
import city.cs.engine.World;
import city.cs.engine.StaticBody;
import city.cs.engine.Shape;
import game.input.Config;
import game.character.Player;
import game.input.Listener;
import org.jbox2d.common.Vec2;

import static game.input.Config.resolution;
import static game.main.WindowHandler.view;


public class Game {
	public static World world;
	public static Player player;
	public static float scaleFactor = resolution.x / 1920;

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
		int size = 13;
		float scaledSize = (float) (((40.5 * scaleFactor) / size) / 2);

		Shape shape = new BoxShape(scaledSize * scaleFactor, scaledSize * scaleFactor,
				new Vec2((-24.5f + ((size - 11) * -.1675f)) * scaleFactor, (-24.5f + ((size - 11) * -.1675f)) * scaleFactor));

		// Create static bodies for each cell
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
					// Create a static body for the cell
					StaticBody body = new StaticBody(world, shape);
					body.setPosition(new Vec2(((scaledSize * 2) * i) * scaleFactor, ((scaledSize * 2) * j) * scaleFactor));
				}
			}
		}

		// Make a character (with an overlaid image)
		player = new Player(world, new Vec2(0, 0));

		// Add Keyboard & Mouse listeners.
		Listener listener = new Listener(player);
		view.addMouseListener(listener);
		view.addKeyListener(listener);
	}
}
