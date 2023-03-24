package game.main;

import city.cs.engine.DebugViewer;
import city.cs.engine.UserView;
import city.cs.engine.World;
import game.input.Config;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static game.main.Game.*;


public class WindowHandler {
	private static final World world = Game.world;
	private static JLayeredPane layeredPane;
	public static final JFrame frame = new JFrame(Config.title);
	public static UserView view = null;

	private static JButton[] menuButtons;
	private static JPanel menuPanel;
	public static boolean inMenu = true;

	public static JLabel scoreLabel;


	public static void createWindow(World world) {
		// Create a user view with the world and resolution provided.
		view = new UserView(world, 0, 0);

		updateWindow();

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(view.getPreferredSize());
		layeredPane.setOpaque(false); // Set the background color to be transparent

		// Add the view to the layered pane at the bottom layer with an explicit position and size.
		layeredPane.add(view, 1);
		view.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);

		// Add the layered pane to the frame.
		frame.setContentPane(layeredPane);

		// Set exit on close.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);
		view.requestFocus();

		// Enable debugs.
		if (Config.DEBUG) {
			new DebugViewer(world, (int) Config.resolution.x, (int) Config.resolution.y);
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

	public static void createOptionsMenu() {

	}

	public static void createLoadMenu() {

	}

	public static void createNewGameMenu() {

	}

	public static void createPauseMenu() {

	}

	public static void createDeathMenu() {

	}

	public static void createGameOverlay() {
		Font font;
		try {
			// Open & register the font with the GraphicsEnvironment
			Font ttf = Font.createFont(Font.TRUETYPE_FONT, new File(Config.font.get("default")));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ttf);
			// TODO: Fix resolution scaling for text.
			font = ttf.deriveFont(Font.PLAIN, (int) Math.round(gridSize * 2.5 * scaledGridSize));
		} catch (IOException | FontFormatException e) {
			font = new Font("Algerian", Font.BOLD, (int) Math.round(gridSize * 2.5 * scaledGridSize));
		}

		// Create a new JPanel to hold the score label.
		JPanel overlayPanel = new JPanel();
		overlayPanel.setOpaque(false); // Transparent

		// Create a new JLabel for the score and add it to the overlay panel.
		scoreLabel = new JLabel("SCORE: " + score, SwingConstants.CENTER);
		scoreLabel.setFont(font);
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setOpaque(false); // Transparent
		overlayPanel.add(scoreLabel);

		// Add the overlay panel to the layered pane at a higher layer than the UserView.
		layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);

		// Set the size of the overlay panel to match the size of the UserView.
		overlayPanel.setSize(view.getPreferredSize().width, scoreLabel.getPreferredSize().height + 10);

		// Adjust the bounds of the scoreLabel to avoid being clipped by the overlayPanel.
		scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 1, 1, 1));
	}

	public static void updateScore() {
		scoreLabel.setText("SCORE: " + score);
	}
}
