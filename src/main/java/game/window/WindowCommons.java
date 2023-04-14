package game.window;

import game.input.Config;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static game.main.Game.scaleFactor;


public class WindowCommons {
	protected static Font loadFont(float size) {
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

	protected static JLabel createText(String text, float scale, JComponent panel, int index) {
		if (!(panel instanceof JLayeredPane || panel instanceof JPanel)) return null;

		JLabel label = new JLabel(text, SwingConstants.CENTER);

		label.setFont(loadFont(scale));
		label.setForeground(Color.WHITE);
		label.setOpaque(false); // Transparent

		panel.add(label, JLayeredPane.DEFAULT_LAYER, index);

		return label;
	}

	protected static JButton createButton(String text, float scale, JComponent panel, int index) {
		if (!(panel instanceof JLayeredPane || panel instanceof JPanel)) return null;

		JButton button = new JButton(text);

		button.setFont(loadFont(scale));
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);

		panel.add(button, JLayeredPane.DEFAULT_LAYER, index);

		return button;
	}
}
