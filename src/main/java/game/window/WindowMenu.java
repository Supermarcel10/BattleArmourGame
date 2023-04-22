package game.window;

import javax.swing.*;
import java.awt.*;


public class WindowMenu extends WindowHandler {
	protected static JButton[] btnsMenu;
	protected static JPanel pnlMenu;

	public static void createMenu() {
		pnlMenu = new JPanel();
		btnsMenu = new JButton[] {
				new JButton("Play"),
				new JButton("Create"),
				new JButton("Options"),
				new JButton("Exit")
		};

		Font font = loadFont(1f);

		for (JButton button : btnsMenu) {
			button.setFont(font);
			button.setSize(500, 500);

			button.setBackground(Color.WHITE);
			button.setForeground(Color.BLACK);

			button.setBounds(0, 0, 500, 200);
			pnlMenu.add(button);
		}

		pnlMenu.setBackground(Color.BLACK);

		pnlMain.add(pnlMenu, JLayeredPane.FRAME_CONTENT_LAYER);
		pnlMenu.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);

//		root.add(pnlMenu);
	}
}
