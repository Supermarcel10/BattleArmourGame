package game.window;

import game.IO.Config;
import game.objects.block.BlockType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static game.Game.gridSize;
import static game.Game.scaleFactor;
import static game.window.WindowHandler.*;


public class WindowBuild {
	protected static JLabel lblHeader;
	protected static JLabel lblInformation;


	public static void createBuildOverlay() {
		// Set the default starting directory to the project directory and file name to NAME.level
		fileChooser.setSelectedFile(new File("NAME.level"));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\levels"));

		// Create the overlay, hide old labels.
		if (lblScore != null) lblScore.setVisible(false);

		lblHeader = createText(String.valueOf(BlockType.BRICK), 1, pnlOverlay, 1);
		int lblPlacementBlockX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
		int lblPlacementBlockY = (int) (2 * scaleFactor);
		lblHeader.setBounds(lblPlacementBlockX, lblPlacementBlockY, (int) Config.resolution.x, (int) Config.resolution.y / gridSize);

		lblInformation = createText("ESC to Save & Exit. BACKSPACE to Exit. L to LOAD.", 0.2f, new Color(200, 30, 30, 255), pnlOverlay, 1);
		int lblExitBuildModeX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
		int lblExitBuildModeY = (int) ((Config.resolution.y / 2) - (35 * scaleFactor));
		lblInformation.setBounds(lblExitBuildModeX, lblExitBuildModeY, (int) Config.resolution.x, (int) Config.resolution.y);
	}

	public static void removeBuildOverlay() {
		lblHeader.setVisible(false);
		lblInformation.setVisible(false);

		lblHeader = null;
		lblInformation = null;
	}

	public static @Nullable File selectSaveLocation() {
		int result = fileChooser.showSaveDialog(view);
		return result == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
	}

	public static void updateBlockPlacement(String blockType) {
		lblHeader.setText(blockType);
	}

	public static void updateInformation(String text) {
		lblInformation.setText(text);
	}
}
