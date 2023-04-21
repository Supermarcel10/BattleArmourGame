package game.window;

import city.cs.engine.DebugViewer;
import city.cs.engine.UserView;
import city.cs.engine.World;
import game.IO.Config;
import game.MainGame;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;

import static game.MainGame.*;


public class WindowHandler extends WindowCommons {
	private static final World world = MainGame.world;

	protected static final JLayeredPane pnlMain = new JLayeredPane();
	protected static final JLayeredPane pnlOverlay = new JLayeredPane();
	public static final JFrame root = new JFrame(Config.title);
	public static UserView view = new UserView(world, 0, 0);

	protected static JButton[] btnsMenu;
	protected static JPanel pnlMenu;
	public static boolean inMenu = true;

	protected static JLabel lblScore;

	public static void createWindow(World world) {
		updateWindow();

		pnlMain.setPreferredSize(view.getPreferredSize());
		pnlMain.setOpaque(false); // Set the background color to be transparent

		// Add the view to the layered pane at the bottom layer with an explicit position and size.
		pnlMain.add(view, JLayeredPane.FRAME_CONTENT_LAYER);
		view.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);

		// Add the layered pane to the frame.
		root.setContentPane(pnlMain);

		// Set exit on close.
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		root.pack();
		root.setVisible(true);
		view.requestFocus();

		// OVERLAY PANEL
		pnlOverlay.setOpaque(false);
		pnlMain.add(pnlOverlay, JLayeredPane.PALETTE_LAYER);
		pnlOverlay.setSize(view.getPreferredSize());

		// Enable debugs.
		if (Config.DEBUG_DRAW) {
			new DebugViewer(world, (int) Config.resolution.x, (int) Config.resolution.y);
			view.setGridResolution(3.75f);
			view.add(new JTextField(String.valueOf(world.getSimulationSettings().getFrameRate())));
		}

		WindowMenu.createMenu();
	}

	public static void updateWindow() {
		// Get the default screen device
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (Config.fullscreen) {
			// Enter full screen.
			device.setFullScreenWindow(root);
			// TODO: BUG: The game window is not the same size as the screen.
			view.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
		} else {
			// Exit full screen.
			device.setFullScreenWindow(null);

			root.setLocationByPlatform(true);
			root.setResizable(false);

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
			root.setLocation((int) (screenSize.width - resolution.x) / 2, (int) (screenSize.height - resolution.y) / 2);
		}
	}

	public static void createOptionsMenu() {

	}

	public static void createLoadMenu() {

	}

	public static void createNewGameMenu() {

	}

	public static void createPauseMenu() {

	}

	public static void createGameOverlay() {
		if (lblScore == null) {
			lblScore = createText("SCORE: " + score, 1, pnlOverlay, 1);
			int lblScoreX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
			int lblScoreY = (int) (2 * scaleFactor);
			lblScore.setBounds(lblScoreX, lblScoreY, (int) Config.resolution.x, (int) Config.resolution.y / gridSize);
		} else {
			// Make the scoreLabel visible again.
			lblScore.setVisible(true);
		}
	}

	public static void updateScore() {
		lblScore.setText("SCORE: " + score);
	}
}
