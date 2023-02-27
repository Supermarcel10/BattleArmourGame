package game;

import city.cs.engine.UserView;
import city.cs.engine.World;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;


public class WindowHandler {
	private static final World world = GameHandler.world;
	public static final JFrame frame = new JFrame(Config.title);
	public static UserView view = null;


	public static void createWindow(World world) {
		// Create a user view with the world and resolution provided.
		view = new UserView(world, 0, 0);

		updateWindow();

		frame.add(view);

		// Size the frame to fill the view.
		frame.pack();

		// Set exit on close.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
		view.requestFocus();

		// Enable debugs.
		if (Config.DEBUG) {
			view.setGridResolution(3);
			view.add(new JTextField(String.valueOf(world.getSimulationSettings().getFrameRate())));
		}
	}


	public static void updateWindow() {
		// Get the default screen device
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (Config.fullscreen) {
			// Enter full screen.
			device.setFullScreenWindow(frame);
		} else {
			// Exit full screen.
			device.setFullScreenWindow(null);

			frame.setLocationByPlatform(true);
			frame.setResizable(false);

			// Get the screen size.
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			// Set the game resolution.
			HashMap<String, Integer> resolution = Config.resolution;

			if (resolution.get("x") > resolution.get("y")) {
				view = new UserView(world, Config.resolution.get("y"), Config.resolution.get("y"));
			} else if (resolution.get("x") < resolution.get("y")) {
				view = new UserView(world, Config.resolution.get("x"), Config.resolution.get("x"));
			}

			// Center the game window.
			frame.setLocation((screenSize.width - Config.resolution.get("x")) / 2, (screenSize.height - Config.resolution.get("y")) / 2);
		}
	}
}
