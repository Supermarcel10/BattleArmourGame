package game.window;

import city.cs.engine.DebugViewer;
import city.cs.engine.UserView;
import city.cs.engine.World;
import game.IO.AM;
import game.IO.Config;
import game.MainGame;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;

import static game.IO.Config.resolution;


/**
 * This class handles the window and the user view.
 */
public class WindowHandler extends WindowCommons {
	private static final World world = MainGame.world;

	protected static final JLayeredPane pnlMain = new JLayeredPane();
	protected static final JLayeredPane pnlOverlay = new JLayeredPane();
	public static final JFrame root = new JFrame("Battle Armour");
	public static UserView view = new UserView(world, 0, 0);

	/**
	 * Creates the initial window.
	 */
	public static void createWindow() {
		updateWindow();

		pnlMain.setPreferredSize(view.getPreferredSize());
		pnlMain.setOpaque(false); // Set the background color to be transparent

		// Add the view to the layered pane at the bottom layer with an explicit position and size.
		pnlMain.add(view, JLayeredPane.FRAME_CONTENT_LAYER);
		view.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);

		// Add the layered pane to the frame.
		root.setContentPane(pnlMain);

		// Set the icon.
		root.setIconImage(new ImageIcon(AM.image.get("windowIcon")).getImage());

		// Set exit on close.
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		root.pack();
		root.setVisible(true);
		view.requestFocus();

		// OVERLAY PANEL
		pnlOverlay.setOpaque(false);
		pnlMain.add(pnlOverlay, JLayeredPane.PALETTE_LAYER);
		pnlOverlay.setSize(view.getPreferredSize());

		WindowMenu.createMenu();
	}

	/**
	 * Updates the window.
	 */
	public static void updateWindow() {
		// Get the default screen device
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (Config.fullscreen) {
			// Enter full screen.
			device.setFullScreenWindow(root);
			// TODO: BUG: The game window is not the same size as the screen.
			view.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());

			resolution = new Vec2(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
		} else {
			// Exit full screen.
			device.setFullScreenWindow(null);

			root.setLocationByPlatform(true);
			root.setResizable(false);

			// Create the user view. Round the resolution to the nearest multiple of gridSize to reduce pixel peaking.
			if (resolution.x > resolution.y) resolution.x = resolution.y;
			else resolution.y = resolution.x;

			view = new UserView(world, (int) resolution.x, (int) resolution.y);

			// Center the game window.
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			root.setLocation((screenSize.width - (int) resolution.x) / 2, (screenSize.height - (int) resolution.y) / 2 - 50);
		}
	}
}
