package game.window;

import game.input.Config;
import game.prefab.BlockType;

import javax.swing.*;
import java.awt.*;

import static game.main.Game.gridSize;
import static game.main.Game.scaleFactor;
import static game.window.WindowHandler.*;

public class WindowBuild {
	protected static JLabel lblPlacementBlock;
	protected static JLabel lblExitBuildMode;

	public static void createBuildOverlay() {
		if (lblScore != null) lblScore.setVisible(false);

		lblPlacementBlock = createText(String.valueOf(BlockType.BRICK), 1, pnlOverlay, 1);
		int lblPlacementBlockX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
		int lblPlacementBlockY = (int) (2 * scaleFactor);
		lblPlacementBlock.setBounds(lblPlacementBlockX, lblPlacementBlockY, (int) Config.resolution.x, (int) Config.resolution.y / gridSize);

		lblExitBuildMode = createText("Press ESC to exit build mode", 0.2f, new Color(200, 30, 30, 255), pnlOverlay, 1);
		int lblExitBuildModeX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
		int lblExitBuildModeY = (int) ((Config.resolution.y / 2) - (150 * scaleFactor));
		lblExitBuildMode.setBounds(lblExitBuildModeX, lblExitBuildModeY, (int) Config.resolution.x, (int) Config.resolution.y);
	}

	public static void removeBuildOverlay() {
		lblPlacementBlock.setVisible(false);
		lblExitBuildMode.setVisible(false);

		lblPlacementBlock = null;
		lblExitBuildMode = null;
	}

	public static void updateBlockPlacement(BlockType blockType) {
		lblPlacementBlock.setText(String.valueOf(blockType));
	}
}
