package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import static game.WindowHandler.view;


public class GameHandler {
	public static World world;
	public static Player player;


	public GameHandler() {
		// Add a shutdown hook.
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// TODO: Add saving to the game.
			// TODO: Add any database close statements here.
			// Run garbage collection.
			System.gc();
		}));


		world = new World();

		// Disable the gravity.
		world.setGravity(0);

		// Change the frame rate to the desired frame rate.
		world.getSimulationSettings().setTargetFrameRate(Config.fps);

		// Create the window and main menu.
		WindowHandler.createWindow(world);
		MainMenu.createMenu(WindowHandler.frame);

		while (MainMenu.inMenu) {
			// TODO: Make menu handling.

			MainMenu.inMenu = false;
		}

		startGame();

		// Start world simulation
		world.start();
	}


	public static void startGame() {
		float scaleFactor = Config.resolution.x / 1920;

		// Make a ground platform
		Shape shape = new BoxShape(30 * scaleFactor, 0.5f  * scaleFactor);
		StaticBody ground = new StaticBody(world, shape);
		ground.setPosition(new Vec2(0f * scaleFactor, -11.5f * scaleFactor));

		// Make a suspended platform
		Shape platformShape = new BoxShape(3 * scaleFactor, 0.5f  * scaleFactor);
		StaticBody platform1 = new StaticBody(world, platformShape);
		platform1.setPosition(new Vec2(8 * scaleFactor, 0 * scaleFactor));
		platform1.setAngle(70);

		// Make a suspended platform
		StaticBody platform2 = new StaticBody(world, platformShape);
		platform2.setPosition(  new Vec2(-6 * scaleFactor, -4 * scaleFactor));
		platform2.setAngle(50);

		// Make a ball
		CircleShape ball = new CircleShape(3 * scaleFactor, 7 * scaleFactor, 20 * scaleFactor);


		// Make a character (with an overlaid image)
		player = new Player(world);

		// Add Keyboard & Mouse listeners.
		Listener listener = new Listener(player);
		view.addMouseListener(listener);
		view.addKeyListener(listener);
	}


	public static void main(String[] args) {
		new GameHandler();
	}
}
