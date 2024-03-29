package game.window;

import game.IO.Config;
import game.main.GameState;
import game.main.LevelCreator;
import game.window.customAssets.CustomButtonUI;
import game.window.customAssets.CustomLayeredPane;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static game.MainGame.*;


/**
 * This class is responsible for creating the menu and handling the menu buttons.
 * <p>
 *     The menu is created by adding buttons to a layered pane.
 *     The buttons are added to a hash map with a runnable as the value.
 *     The runnable is executed when the button is pressed.
 * </p>
 */
public class WindowMenu extends WindowHandler {
	protected static CustomLayeredPane pnlMenu = new CustomLayeredPane(new Color(224, 242, 203));
	protected static HashMap<JButton, Runnable> btnsMenu = new LinkedHashMap<>() {{
		put(createButton("PLAY", .8f, pnlMenu, 10), WindowMenu::play);
		put(createButton("CREATE", .8f, pnlMenu, 10), WindowMenu::create);
		put(createButton("HIGH SCORE", .8f, pnlMenu, 10), WindowMenu::highScore);
		put(createButton("OPTIONS", .8f, pnlMenu, 10), WindowMenu::options);
		put(createButton("EXIT", .8f, pnlMenu, 10), () -> System.exit(0));
	}};

	/**
	 * Creates the menu and adds it to the main panel.
	 */
	public static void createMenu() {
		gameState = GameState.MENU;

		int btnMainMenuWidth = (int) (Config.resolution.x * 0.8f);
		int btnMainMenuHeight = (int) ((Config.resolution.y / gridSize) * 1.5f);
		int btnMainMenuX = (pnlMain.getPreferredSize().width - btnMainMenuWidth) / 2;
		int pnlMainHeight = pnlMain.getPreferredSize().height;

		// Set the position and UI of each button in the same order they were added.
		int btnY = pnlMainHeight - (btnMainMenuHeight * (btnsMenu.size() + 2));
		Color pressedColor = new Color(0, 40, 0, 100); // Button pressed color.

		for (JButton button : btnsMenu.keySet()) {
			button.setBounds(btnMainMenuX, btnY += btnMainMenuHeight, btnMainMenuWidth, btnMainMenuHeight);
			button.setUI(new CustomButtonUI(pressedColor));

			// Add action listener to each button
			button.addActionListener(e -> btnsMenu.get(button).run());
		}

		// Title // TODO: Fix title bar not showing up properly.
//		try {
//			ImageIcon imageIcon = new ScaledImageIcon(AM.image.get("title"), pnlMain.getPreferredSize().width, 400);
//
//			JLabel title = new JLabel(imageIcon);
//			pnlMenu.add(title, CustomLayeredPane.DEFAULT_LAYER);
//			title.setBounds(0, 0, pnlMain.getPreferredSize().width, (int) (400 * scaleFactor));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// Add the menu to the main panel.
		pnlMain.add(pnlMenu, CustomLayeredPane.DEFAULT_LAYER);
		pnlMenu.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);
	}

	/**
	 * Shows the menu.
	 */
	public static void showMenu() {
		gameState = GameState.MENU;
		pnlMenu.setVisible(true);
	}

	/**
	 * Hides the menu.
	 */
	public static void hideMenu() {
		pnlMenu.setVisible(false);
	}

	/**
	 * Removes the menu from the main panel.
	 * Runs the play window.
	 */
	private static void play() {
		hideMenu();
		WindowPlay.selectNumOfPlayers();
	}

	/**
	 * Removes the menu from the main panel.
	 * Runs the level creator window.
	 */
	private static void create() {
		gameState = GameState.EDITOR;
		hideMenu();
		LevelCreator.createLevel();
	}

	/**
	 * Removes the menu from the main panel.
	 * Runs the high score window.
	 */
	private static void highScore() {
		gameState = GameState.HIGHSCORE;
		hideMenu();
		WindowHighScore.createHighScoreOverlay();
	}

	/**
	 * Removes the menu from the main panel.
	 * Runs the options window.
	 */
	private static void options() {
		// TODO: Implement options menu.
//		gameState = GameState.OPTIONS;
//		removeMenu();
//		WindowOptions.createOptions();
	}
}
