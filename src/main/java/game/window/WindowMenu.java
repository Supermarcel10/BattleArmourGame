package game.window;

import game.IO.Config;
import game.MainGame;
import game.main.GameState;
import game.main.LevelCreator;
import game.window.customAssets.CustomButtonUI;
import game.window.customAssets.CustomLayeredPane;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static game.MainGame.gameState;
import static game.MainGame.gridSize;


public class WindowMenu extends WindowHandler {
	protected static CustomLayeredPane pnlMenu = new CustomLayeredPane(new Color(0, 40, 0, 40));
	protected static HashMap<JButton, Runnable> btnsMenu = new LinkedHashMap<>() {{
		put(createButton("EXIT", .8f, pnlMenu, 10), () -> System.exit(0));
		put(createButton("OPTIONS", .8f, pnlMenu, 10), WindowMenu::options);
		put(createButton("HIGH SCORE", .8f, pnlMenu, 10), WindowMenu::highScore);
		put(createButton("CREATE", .8f, pnlMenu, 10), WindowMenu::create);
		put(createButton("PLAY", .8f, pnlMenu, 10), WindowMenu::play);
	}};

	public static void createMenu() {
		gameState = GameState.MENU;

		int btnMainMenuWidth = (int) Config.resolution.x / 2;
		int btnMainMenuHeight = (int) ((Config.resolution.y / gridSize) * 1.5f);
		int btnMainMenuX = (pnlMain.getPreferredSize().width - btnMainMenuWidth) / 2;
		int pnlMainHeight = pnlMain.getPreferredSize().height;

		// Set the position and UI of each button in the same order they were added.
		int btnY = pnlMainHeight - btnMainMenuHeight; // Set initial Y position from bottom of screen.
		Color pressedColor = new Color(0, 40, 0, 100); // Button pressed color.

		for (JButton button : btnsMenu.keySet()) {
			button.setBounds(btnMainMenuX, btnY -= btnMainMenuHeight, btnMainMenuWidth, btnMainMenuHeight);
			button.setUI(new CustomButtonUI(pressedColor));
		}

		// Add action listener to each button
		for (JButton button : btnsMenu.keySet()) {
			button.addActionListener(e -> {
				btnsMenu.get(button).run();
			});
		}

		// Add the menu to the main panel.
		pnlMain.add(pnlMenu, CustomLayeredPane.DEFAULT_LAYER);
		pnlMenu.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);
	}

	public static void showMenu() {
		gameState = GameState.MENU;
		pnlMenu.setVisible(true);
	}

	public static void hideMenu() {
		pnlMenu.setVisible(false);
	}

	private static void play() {
		gameState = GameState.GAME;
		hideMenu();
		MainGame.loadGame();
	}

	private static void create() {
		gameState = GameState.EDITOR;
		hideMenu();
		LevelCreator.createLevel();
	}

	private static void highScore() {
		return;
		// TODO: Implement highScore menu.
//		gameState = GameState.OPTIONS;
//		removeMenu();
//		WindowOptions.createOptions();
	}

	private static void options() {
		return;
		// TODO: Implement options menu.
//		gameState = GameState.OPTIONS;
//		removeMenu();
//		WindowOptions.createOptions();
	}
}
