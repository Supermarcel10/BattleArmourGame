package game.window.customAssets;

import javax.swing.*;
import java.awt.*;


public class CustomLayeredPane extends JLayeredPane {
	private final Color bgColor;

	public CustomLayeredPane(Color bgColor) {
		this.bgColor = bgColor;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
