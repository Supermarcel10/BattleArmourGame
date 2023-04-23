package game.window;

import game.IO.Config;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static game.MainGame.scaleFactor;
import static game.window.WindowHandler.view;


public class WindowCommons {
	public static final JFileChooser fileChooser = new JFileChooser();

	protected static Font loadFont(float size) {
		Font font;

		try {
			// Open & register the font with the GraphicsEnvironment
			Font ttf = Font.createFont(Font.TRUETYPE_FONT, new File(Config.font.get("default")));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ttf);
			font = ttf.deriveFont(Font.PLAIN, Math.round(scaleFactor * (64 * size) - 4));
		} catch (IOException | NullPointerException | FontFormatException e) {
			font = new Font("Algerian", Font.BOLD, Math.round(scaleFactor * (64 * size) - 4));
		}

		return font;
	}

	protected static JLabel createText(String text, float scale, Color color, JComponent panel, int index) {
		if (!(panel instanceof JLayeredPane || panel instanceof JPanel)) return null;

		JLabel label = new JLabel(text, SwingConstants.CENTER);

		label.setFont(loadFont(scale));
		label.setForeground(color);
		label.setOpaque(false); // Transparent

		panel.add(label, JLayeredPane.DEFAULT_LAYER, index);

		return label;
	}

	protected static JLabel createText(String text, float scale, JComponent panel, int index) {
		return createText(text, scale, Color.WHITE, panel, index);
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

	public static @Nullable File selectLoadFile() {
		int result = fileChooser.showOpenDialog(view);
		return result == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
	}
}
