package game.main;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.input.Config;
import game.character.Player;
import game.input.Listener;
import org.jbox2d.common.Vec2;

import java.awt.*;

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
//		double size = 13f;
//
//		// Calculate the width and height of each cell
//		double cellWidth = resolution.x / size;
//		double cellHeight = resolution.y / size;
//
//		System.out.println(cellWidth);
//		System.out.println(cellHeight);
//
//		Shape shape = new BoxShape((float) 2, (float) 1);
////		StaticBody ground = new StaticBody(world, shape);
////		ground.setPosition(new Vec2(-26.25f * scaleFactor, 26.25f * scaleFactor));
//
//		// Create static bodies for each cell
//		for (int i = 0; i < size; i++) {
//			for (int j = 0; j < size; j++) {
//				if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
//					float x = (float) ((((cellWidth * i) / 1920.0) * 52.5) - 26.25);
//					float y = (float) ((((cellHeight * j) / 1080.0) * 52.5) - 26.25);
//
////					System.out.println("x: " + x + ", y: " + y);
//
//					// Create a static body for the cell
//					StaticBody body = new StaticBody(world, shape);
//					body.setPosition(new Vec2(0f, 0));
//					return;
//				}
//			}
//		}

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

		// Make a character (with an overlaid image)
		player = new Player(world, new Vec2(0, 0));

		// Add Keyboard & Mouse listeners.
		Listener listener = new Listener(player);
		view.addMouseListener(listener);
		view.addKeyListener(listener);
	}
}
