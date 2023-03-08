package game.main;

import city.cs.engine.World;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class MainMenu {
	private static final World world = Game.world;
	private static JButton[] buttons;
	private static JPanel panel;
	public static boolean inMenu = true;

	public static void createMenu(@NotNull Frame frame) {
		panel = new JPanel();
		buttons = new JButton[] {
			new JButton("New Game"),
			new JButton("Load Game"),
			new JButton("Options")
		};

		Font font = new Font("Arial", Font.PLAIN, 80);

		for (JButton button : buttons) {
			button.setFont(font);
			button.setSize(500, 500);

			button.setBackground(Color.WHITE);
			button.setForeground(Color.BLACK);

			panel.add(button);
		}

		frame.add(panel);

		// TODO: Change
//		changeMenuState(false);
	}

	public static void changeMenuState(boolean state) {
		panel.setVisible(state);
		for (JButton button : buttons) {
			button.setVisible(state);
		}

		inMenu = state;
	}
}
