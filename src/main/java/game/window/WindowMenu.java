package game.window;

import javax.swing.*;
import java.awt.*;

public class WindowMenu extends WindowHandler {
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

	protected static void changeMenuState(boolean state) {
		pnlMenu.setVisible(state);
		for (JButton button : btnsMenu) {
			button.setVisible(state);
		}

		inMenu = state;
	}
}
