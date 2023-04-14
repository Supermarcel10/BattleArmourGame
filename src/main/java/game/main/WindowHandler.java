package game.main;

import city.cs.engine.DebugViewer;
import city.cs.engine.UserView;
import city.cs.engine.World;
import game.input.Config;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static game.main.Game.*;


public class WindowHandler {
	private static final World world = Game.world;
	private static final JLayeredPane pnlMain = new JLayeredPane();
	private static final JLayeredPane pnlOverlay = new JLayeredPane();
	public static final JFrame root = new JFrame(Config.title);
	public static UserView view = new UserView(world, 0, 0);

	private static JButton[] btnsMenu;
	private static JPanel pnlMenu;
	public static boolean inMenu = true;

	public static JLabel lblScore;

	private static Font loadFont(float size) {
		Font font;

		try {
			// Open & register the font with the GraphicsEnvironment
			Font ttf = Font.createFont(Font.TRUETYPE_FONT, new File(Config.font.get("default")));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ttf);
			font = ttf.deriveFont(Font.PLAIN, Math.round(scaleFactor * (64 * size) - 4));
		} catch (IOException | FontFormatException e) {
			font = new Font("Algerian", Font.BOLD, Math.round(scaleFactor * (64 * size) - 4));
		}

		return font;
	}

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

	public static void createMenu() {
		pnlMenu = new JPanel();
		btnsMenu = new JButton[] {
			new JButton("New Game"),
			new JButton("Load Game"),
			new JButton("Options")
		};

		Font font = new Font("Arial", Font.PLAIN, 80);

		for (JButton button : btnsMenu) {
			button.setFont(font);
			button.setSize(500, 500);

			button.setBackground(Color.WHITE);
			button.setForeground(Color.BLACK);

			pnlMenu.add(button);
		}

		root.add(pnlMenu);

		// TODO: Change once implemented.
//		changeMenuState(false);
	}

	public static void changeMenuState(boolean state) {
		pnlMenu.setVisible(state);
		for (JButton button : btnsMenu) {
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
		lblScore.setVisible(false);

		pnlOverlay.setBackground(new Color(0.9f, 0, 0, 0));
		pnlOverlay.setOpaque(true);

		final boolean[] complete = new boolean[]{false, false};

		new Timer(10, new ActionListener() {
			float alpha = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				alpha += 0.005f;
				if (alpha >= 0.3f) {
					alpha = 1f;
					complete[0] = true;
					((Timer) e.getSource()).stop();
				}
				pnlOverlay.setBackground(new Color(0.9f, 0, 0, alpha));
				pnlOverlay.repaint();
			}
		}).start();

		JLabel lblGameOver = createText("GAME OVER!", 1.25f, pnlOverlay, 0);

		int gameOverLabelWidth = (int) Config.resolution.x;
		int gameOverLabelHeight = (int) (Config.resolution.y / gridSize) * 2;
		int gameOverLabelX = (pnlMain.getPreferredSize().width - gameOverLabelWidth) / 2;

		int startGameOverLabelY = (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 1f));
		int endGameOverLabelY = (int) ((Config.resolution.y / gridSize) + (2f * scaleFactor));

		lblGameOver.setBounds(gameOverLabelX, startGameOverLabelY, gameOverLabelWidth, gameOverLabelHeight);

		// Animate the game over label.
		Timer timer = new Timer(10, e -> {
			if (complete[0] && lblGameOver.getY() > endGameOverLabelY) {
				pnlOverlay.repaint();
				lblGameOver.setLocation(lblGameOver.getX(), (int) (lblGameOver.getY() - 4.5f * scaleFactor));
			} else if (complete[0]) {
				complete[1] = true;
				((Timer) e.getSource()).stop();
			}
		});

		timer.setInitialDelay(400);
		timer.start();

		JLabel lblScore = createText("Your score: " + score, 0.55f, pnlOverlay, 0);
		lblScore.setVisible(false);
		lblScore.setBounds(gameOverLabelX, (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 4f)), gameOverLabelWidth, gameOverLabelHeight);

		// TODO: Implement
		JLabel lblBlockBrokenScore = createText("Tanks destroyed: " + score, 0.55f, pnlOverlay, 0);
		lblBlockBrokenScore.setVisible(false);
		lblBlockBrokenScore.setBounds(gameOverLabelX, (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 3f)), gameOverLabelWidth, gameOverLabelHeight);

		// TODO: Implement
		JLabel lblKillScore = createText("Blocks destroyed: " + score, 0.55f, pnlOverlay, 0);
		lblKillScore.setVisible(false);
		lblKillScore.setBounds(gameOverLabelX, (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 2f)), gameOverLabelWidth, gameOverLabelHeight);

		JButton btnMainMenu = createButton("Main Menu", .8f, pnlOverlay, 10);
		btnMainMenu.setVisible(false);
		JButton btnAddHighScore = createButton("Add High Score", .8f, pnlOverlay, 10);
		btnAddHighScore.setVisible(false);

		int btnMainMenuWidth = (int) Config.resolution.x / 2;
		int btnMainMenuHeight = (int) ((Config.resolution.y / gridSize) * 1.25f);
		int btnMainMenuX = (pnlMain.getPreferredSize().width - btnMainMenuWidth) / 2;
		int btnMainMenuY = (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) + 1f));

		int btnAddHighScoreWidth = (int) Config.resolution.x / 2;
		int btnAddHighScoreHeight = (int) ((Config.resolution.y / gridSize) * 1.25f);
		int btnAddHighScoreX = (pnlMain.getPreferredSize().width - btnAddHighScoreWidth) / 2;
		int btnAddHighScoreY = (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) + 3f));

		btnMainMenu.setBounds(btnMainMenuX, btnMainMenuY, btnMainMenuWidth, btnMainMenuHeight);
		btnAddHighScore.setBounds(btnAddHighScoreX, btnAddHighScoreY, btnAddHighScoreWidth, btnAddHighScoreHeight);

		Timer t2 = new Timer(10, e -> {
			if (complete[1]) {
				lblScore.setVisible(true);
				lblBlockBrokenScore.setVisible(true);
				lblKillScore.setVisible(true);
				btnMainMenu.setVisible(true);
				btnAddHighScore.setVisible(true);
				((Timer) e.getSource()).stop();
			}
		});

		t2.setInitialDelay(400);
		t2.start();

		btnMainMenu.addActionListener(e -> {
			changeMenuState(true);
			pnlOverlay.setVisible(false);
		});

		btnAddHighScore.addActionListener(e -> {

		});
	}

	public static void createGameOverlay() {
		if (lblScore == null) {
			lblScore = createText("SCORE: " + score, 1, pnlOverlay, 1);

			int lblScoreWidth = (int) Config.resolution.x;
			int lblScoreHeight = (int) Config.resolution.y / gridSize;
			int lblScoreX = (pnlMain.getPreferredSize().width - lblScoreWidth) / 2;
			int lblScoreY = (int) (2 * scaleFactor);

			lblScore.setBounds(lblScoreX, lblScoreY, lblScoreWidth, lblScoreHeight);
		} else {
			// Make the scoreLabel visible again.
			lblScore.setVisible(true);
		}
	}

	public static void updateScore() {
		lblScore.setText("SCORE: " + score);
	}

	private static JLabel createText(String text, float scale, JComponent panel, int index) {
		if (!(panel instanceof JLayeredPane || panel instanceof JPanel)) return null;

		JLabel label = new JLabel(text, SwingConstants.CENTER);

		label.setFont(loadFont(scale));
		label.setForeground(Color.WHITE);
		label.setOpaque(false); // Transparent

		panel.add(label, JLayeredPane.DEFAULT_LAYER, index);

		return label;
	}

	private static JButton createButton(String text, float scale, JComponent panel, int index) {
		if (!(panel instanceof JLayeredPane || panel instanceof JPanel)) return null;

		JButton button = new JButton(text);

		button.setFont(loadFont(scale));
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);

		panel.add(button, JLayeredPane.DEFAULT_LAYER, index);

		return button;
	}
}
