package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import static game.WindowHandler.view;


public class GameHandler {
	public static World world;


	public GameHandler() {
		// Add a shutdown hook.
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// TODO: Add saving to the game

			// Run garbage collection.
			System.gc();
		}));


		world = new World();

		// Change the frame rate to the desired frame rate.
		world.getSimulationSettings().setTargetFrameRate(Config.fps);

		// Create the window and main menu
		WindowHandler.createWindow(world);
		MainMenu.createMenu(WindowHandler.frame);

		// Add Keyboard & Mouse listeners
		GiveFocus gf = new GiveFocus(view);
		view.addMouseListener(gf);
		view.addKeyListener(new Listener());

		while (MainMenu.inMenu) {
			// TODO: Make menu handling.

			MainMenu.inMenu = false;
		}

		startGame();

		// Start world simulation
		world.start();
	}


	public static void startGame() {
		// Make a ground platform
		Shape shape = new BoxShape(30, 0.5f);
		StaticBody ground = new StaticBody(world, shape);
		ground.setPosition(new Vec2(0f, -11.5f));

		// Make a suspended platform
		Shape platformShape = new BoxShape(3, 0.5f);
		StaticBody platform1 = new StaticBody(world, platformShape);
		platform1.setPosition(new Vec2(8, 0));
		platform1.setAngle(70);

		// Make a suspended platform
		StaticBody platform2 = new StaticBody(world, platformShape);
		platform2.setPosition(  new Vec2(-6, -4));
		platform2.setAngle(50);

		// Make a ball
		CircleShape ball = new CircleShape(3, 7, 20);


		// Make a character (with an overlaid image)
		Shape studentShape = new BoxShape(1,2);
		DynamicBody student = new DynamicBody(world, studentShape);
		student.setPosition(new Vec2(4,10));
//		student.addImage(new BodyImage("data/img/student.png", 4));

		// TODO: Research how movement works!
//		student.move(new Vec2(0, -0.5f));

//		Player player = new Player(100);
	}


	public static void main(String[] args) {
		new GameHandler();
	}
}
