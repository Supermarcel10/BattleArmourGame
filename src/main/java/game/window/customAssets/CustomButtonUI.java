package game.window.customAssets;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;


/**
 * Custom button UI used to change the color of the button when it is pressed.
 */
public class CustomButtonUI extends BasicButtonUI {
	private final Color pressedColor;

	public CustomButtonUI(Color pressedColor) {
		this.pressedColor = pressedColor;
	}

	/**
	 * Paints the button with the pressed color.
	 * @param g an instance of {@code Graphics}
	 * @param b an abstract button
	 */
	@Override
	protected void paintButtonPressed(@NotNull Graphics g, @NotNull AbstractButton b) {
		g.setColor(pressedColor);
		g.fillRect(0, 0, b.getWidth(), b.getHeight());
	}
}
