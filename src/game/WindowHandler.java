package game;

import city.cs.engine.UserView;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

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
			view.setGridResolution(3.75f);
			view.add(new JTextField(String.valueOf(world.getSimulationSettings().getFrameRate())));
		}
	}


	public static void updateWindow() {
		// Get the default screen device
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (Config.fullscreen) {
			// Enter full screen.
			device.setFullScreenWindow(frame);
			// TODO: BUG: The game window is not the same size as the screen.
			view.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
		} else {
			// Exit full screen.
			device.setFullScreenWindow(null);

			frame.setLocationByPlatform(true);
			frame.setResizable(false);

			// Get the screen size.
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			// Set the game resolution.
			Vec2 resolution = Config.resolution;

			if (resolution.x > resolution.y) {
				view = new UserView(world, (int) resolution.y, (int) resolution.y);
			} else if (resolution.x < resolution.y) {
				view = new UserView(world, (int) resolution.x, (int) resolution.x);
			}

			// Center the game window.
			frame.setLocation((int) (screenSize.width - resolution.x) / 2, (int) (screenSize.height - resolution.y) / 2);
		}
	}
}
