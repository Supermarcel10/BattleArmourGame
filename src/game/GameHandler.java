package game;

import character.Player;
import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import javax.swing.JFrame;


/**
 * Your main game entry point
 */
public class GameHandler {
	/** Initialise a new Game. */
	public GameHandler() {
		//1. make an empty game world
		World world = new World();

		//2. populate it with bodies (ex: platforms, collectibles, characters)

		//make a ground platform
		Shape shape = new BoxShape(30, 0.5f);
		StaticBody ground = new StaticBody(world, shape);
		ground.setPosition(new Vec2(0f, -11.5f));

		// make a suspended platform
		Shape platformShape = new BoxShape(3, 0.5f);
		StaticBody platform1 = new StaticBody(world, platformShape);
		platform1.setPosition(new Vec2(8, 0));
		platform1.setAngle(70);

		// make a suspended platform
		StaticBody platform2 = new StaticBody(world, platformShape);
		platform2.setPosition(  new Vec2(-6, -4));
		platform2.setAngle(50);

		// Make a ball
		CircleShape ball = new CircleShape(3, 7, 20);


		//make a character (with an overlaid image)
		Shape studentShape = new BoxShape(1,2);
		DynamicBody student = new DynamicBody(world, studentShape);
		student.setPosition(new Vec2(4,10));
		student.addImage(new BodyImage("data/img/student.png", 4));

		// TODO: Research how movement works!
//		student.move(new Vec2(0, -0.5f));

		Player player = new Player(100);


		//3. make a view to look into the game world
		UserView view = new UserView(world, 500, 500);


		//optional: draw a 1-metre grid over the view
		view.setGridResolution(1);


		//4. create a Java window (frame) and add the game
		//   view to it
		final JFrame frame = new JFrame("City Game");
		frame.add(view);

		// enable the frame to quit the application
		// when the x button is pressed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		// don't let the frame be resized
		frame.setResizable(false);
		// size the frame to fit the world view
		frame.pack();
		// finally, make the frame visible
		frame.setVisible(true);

		//optional: uncomment this to make a debugging view
		JFrame debugView = new DebugViewer(world, 500, 500);

		// start our game world simulation!
		world.start();
	}

	/** Run the game. */
	public static void main(String[] args) {

		new GameHandler();
	}
}
