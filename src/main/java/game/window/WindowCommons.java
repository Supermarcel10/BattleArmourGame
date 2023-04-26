package game.window;

import game.IO.AM;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static game.MainGame.scaleFactor;
import static game.window.WindowHandler.view;


/**
 * A class containing common methods and variables used by the windows.
 */
public class WindowCommons {
	public static final JFileChooser fileChooser = new JFileChooser();

	/**
	 * Loads the font used in the game.
	 * @param size The size of the font.
	 * @return The font.
	 */
	protected static Font loadFont(float size) {
		Font font;

		try {
			// Open & register the font with the GraphicsEnvironment
			Font ttf = Font.createFont(Font.TRUETYPE_FONT, new File(AM.font.get("PressStart2P")));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ttf);
			font = ttf.deriveFont(Font.PLAIN, Math.round(scaleFactor * (64 * size) - 4));
		} catch (IOException | NullPointerException | FontFormatException e) {
			font = new Font("Algerian", Font.BOLD, Math.round(scaleFactor * (64 * size) - 4));
		}

		return font;
	}

	/**
	 * Creates a text label.
	 * @param text The text to display.
	 * @param scale The scale of the text.
	 * @param color The color of the text.
	 * @param panel The parent panel to add the label to.
	 * @param index The index of the label.
	 * @return The label.
	 */
	protected static JLabel createText(String text, float scale, Color color, JComponent panel, int index) {
		if (!(panel instanceof JLayeredPane || panel instanceof JPanel)) return null;

		JLabel label = new JLabel(text, SwingConstants.CENTER);

		label.setFont(loadFont(scale));
		label.setForeground(color);
		label.setOpaque(false); // Transparent

		panel.add(label, JLayeredPane.DEFAULT_LAYER, index);

		return label;
	}

	/**
	 * Creates a text label.
	 * @param text The text to display.
	 * @param scale The scale of the text.
	 * @param panel The parent panel to add the label to.
	 * @param index The index of the label.
	 * @return The label.
	 */
	protected static JLabel createText(String text, float scale, JComponent panel, int index) {
		return createText(text, scale, Color.WHITE, panel, index);
	}

	/**
	 * Creates a button.
	 * @param text The text to display.
	 * @param scale The scale of the text.
	 * @param panel The parent panel to add the button to.
	 * @param index The index of the button.
	 * @return The button.
	 */
	protected static JButton createButton(String text, float scale, JComponent panel, int index) {
		if (!(panel instanceof JLayeredPane || panel instanceof JPanel)) return null;

		JButton button = new JButton(text);

		button.setFont(loadFont(scale));
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFocusPainted(false);
		button.setOpaque(false);

		panel.add(button, JLayeredPane.DEFAULT_LAYER, index);

		return button;
	}

	/**
	 * Opens a file chooser to select a file to save to.
	 * @return The file selected.
	 */
	public static @Nullable File selectLoadFile() {
		int result = fileChooser.showOpenDialog(view);
		return result == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
	}
}
