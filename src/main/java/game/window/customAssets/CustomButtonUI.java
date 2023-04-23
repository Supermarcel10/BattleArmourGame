package game.window.customAssets;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;


public class CustomButtonUI extends BasicButtonUI {
	private final Color pressedColor;

	public CustomButtonUI(Color pressedColor) {
		this.pressedColor = pressedColor;
	}

	@Override
	protected void paintButtonPressed(@NotNull Graphics g, @NotNull AbstractButton b) {
		g.setColor(pressedColor);
		g.fillRect(0, 0, b.getWidth(), b.getHeight());
	}
}
