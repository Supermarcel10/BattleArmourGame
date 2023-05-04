package game.window.customAssets;

import javax.swing.*;
import java.awt.*;


/**
 * Custom JLayeredPane that allows for a background color to be set.
 */
public class CustomLayeredPane extends JLayeredPane {
	private final Color bgColor;

	public CustomLayeredPane(Color bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * Paints the background color.
	 * @param g the <code>Graphics</code> object to protect
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
