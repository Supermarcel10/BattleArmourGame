package game.main;

import city.cs.engine.UserView;
import city.cs.engine.World;
import game.input.Config;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static game.main.Game.gridSize;


public class WindowHandler {
	private static final World world = Game.world;
	public static final JFrame frame = new JFrame(Config.title);
	public static UserView view = null;

	private static JButton[] menuButtons;
	private static JPanel menuPanel;
	public static boolean inMenu = true;

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

		createMenu();
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

			// Create the user view. Round the resolution to the nearest multiple of gridSize to reduce pixel peaking.
			view = resolution.x > resolution.y ?
					new UserView(world,
							(int) (resolution.y / gridSize) * gridSize,
							(int) (resolution.y / gridSize) * gridSize) :
					new UserView(world,
							(int) (resolution.x / gridSize) * gridSize,
							(int) (resolution.x / gridSize) * gridSize) ;

			// Center the game window.
			frame.setLocation((int) (screenSize.width - resolution.x) / 2, (int) (screenSize.height - resolution.y) / 2);
		}
	}

	public static void createMenu() {
		menuPanel = new JPanel();
		menuButtons = new JButton[] {
				new JButton("New Game"),
				new JButton("Load Game"),
				new JButton("Options")
		};

		Font font = new Font("Arial", Font.PLAIN, 80);

		for (JButton button : menuButtons) {
			button.setFont(font);
			button.setSize(500, 500);

			button.setBackground(Color.WHITE);
			button.setForeground(Color.BLACK);

			menuPanel.add(button);
		}

		frame.add(menuPanel);

		// TODO: Change once implemented.
//		changeMenuState(false);
	}

	public static void changeMenuState(boolean state) {
		menuPanel.setVisible(state);
		for (JButton button : menuButtons) {
			button.setVisible(state);
		}

		inMenu = state;
	}
}
