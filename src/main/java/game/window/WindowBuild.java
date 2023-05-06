package game.window;

import game.IO.Config;
import game.objects.block.BlockType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static game.MainGame.gridSize;
import static game.MainGame.scaleFactor;
import static game.window.WindowHandler.*;
import static game.window.WindowPlay.lblScore;


/**
 * This class is used to create the build overlay and handle the file chooser.
 */
public class WindowBuild {
	protected static JLabel lblHeader;
	protected static JLabel lblInformation;

	protected static Timer lblTimerInformation = new Timer(5000, e -> {
		if (lblInformation != null) lblInformation.setText("ESC to Save & Exit. BACKSPACE to Exit. L to LOAD.");
	});

	/**
	 * Create the build overlay and set the default file name and directory.
	 */
	public static void createBuildOverlay() {
		// Set the default starting directory to the project directory and file name to NAME.level
		fileChooser.setSelectedFile(new File("NAME.level"));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/src/main/resources/levels"));

		// Disable timer restarting.
		lblTimerInformation.setRepeats(false);

		// Create the overlay, hide old labels.
		if (lblScore != null) lblScore.setVisible(false);

		lblHeader = createText(String.valueOf(BlockType.BRICK), 1, pnlOverlay, 1);
		int lblPlacementBlockX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
		int lblPlacementBlockY = (int) (2 * scaleFactor);
		lblHeader.setBounds(lblPlacementBlockX, lblPlacementBlockY, (int) Config.resolution.x, (int) Config.resolution.y / gridSize);

		lblInformation = createText("ESC to Save & Exit. BACKSPACE to Exit. L to LOAD.", 0.4f, new Color(200, 30, 30, 255), pnlOverlay, 1);
		int lblExitBuildModeX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
		int lblExitBuildModeY = (int) ((Config.resolution.y / 2) - (35 * scaleFactor));
		lblInformation.setBounds(lblExitBuildModeX, lblExitBuildModeY, (int) Config.resolution.x, (int) Config.resolution.y);
	}

	/**
	 * Remove the build overlay.
	 */
	public static void removeBuildOverlay() {
		lblHeader.setVisible(false);
		lblInformation.setVisible(false);
	}

	/**
	 * Create a file chooser and return the selected file.
	 * @return The selected file.
	 */
	public static @Nullable File selectSaveLocation() {
		int result = fileChooser.showSaveDialog(view);
		return result == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
	}

	/**
	 * Update the header to display the block type.
	 * @param blockType The block type to display.
	 */
	public static void updateBlockPlacement(String blockType) {
		lblHeader.setText(blockType);
	}

	/**
	 * Update the information label.
	 * @param text The text to display.
	 */
	public static void updateInformation(String text) {
		lblInformation.setText(text);
		lblTimerInformation.restart();
	}
}
