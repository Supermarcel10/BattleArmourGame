package game.main;

import game.objects.Block;
import game.prefab.BlockType;
import game.window.GameState;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import static game.main.Game.*;
import static game.main.LoadLevel.loadLevel;
import static game.main.SaveLevel.saveLevel;
import static game.objects.Block.removeIfExists;
import static game.window.WindowBuild.*;
import static game.window.WindowCommons.selectLoadFile;
import static game.window.WindowHandler.view;


public class CreateLevel implements MouseListener, KeyListener {
	private static BlockType currentBlockType = BlockType.BRICK;
	private static Vec2 basePos;


	public CreateLevel() {
		scaledGridSize = (((27 * scaleFactor) / gridSize) / scaleFactor);
		blocks = new Block[gridSize][gridSize];

		// Add the mouse and keyboard listeners.
		view.addMouseListener(this);
		view.addKeyListener(this);

		// Create the overlay.
		createBuildOverlay();

		// Always make a border around the map.
		for (int i = 0; i < gridSize; i++) {
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, -hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize, -hGridSize + i), true);
			new Block(BlockType.EDGE, new Vec2(hGridSize, -hGridSize + i), true);
		}
	}

	public void finish() {
		// Let the user select save location and attempt saving to it.
		try {
			saveLevel(selectSaveLocation());
		} catch (Exception ignored) {
			updateInformation("Failed to save level! Press ESC to try again. Press BACKSPACE to cancel.");
			return;
		}

		// If successful, exit the level editor.
		exit();
	}

	private void exit() {
		// Remove the mouse and keyboard listeners.
		view.removeMouseListener(this);
		view.removeKeyListener(this);

		// Remove the overlay.
		removeBuildOverlay();

		// Reset the game and go back to the menu.
		resetGame();
		gameState = GameState.MENU;
	}

	@Override
	public void mouseClicked(@NotNull MouseEvent e) {
		// Translate the mouse to start from the center of the screen.
		e.translatePoint(-view.getWidth() / 2, -view.getHeight() / 2);

		Vec2 pos = new Vec2(
				Math.round(e.getPoint().x / ((float) view.getWidth() / gridSize)),
				-Math.round(e.getPoint().y / ((float) view.getHeight() / gridSize))
		);

		if (e.getButton() == MouseEvent.BUTTON1) placeBlock(pos);
		else if (e.getButton() == MouseEvent.BUTTON3) cycleBlockType();
	}

	private void placeBlock(Vec2 pos) {
		removeIfExists(pos);

		if (currentBlockType == BlockType.NONE) return;
		else if (currentBlockType == BlockType.BASE) {
			// Remove the old base if it exists.
			if (basePos != null) removeIfExists(basePos);

			// Set the new base location.
			basePos = pos;
		}

		// Place the block.
		new Block(currentBlockType, pos, true);
	}

	private void cycleBlockType() {
		// Cycle through the block types.
		currentBlockType = BlockType.values()[(currentBlockType.ordinal() + 1) % BlockType.values().length];
		updateBlockPlacement(currentBlockType);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		view.requestFocus();
	}

	@Override
	public void keyTyped(@NotNull KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE) finish(); // ESC to exit.
		else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) exit(); // BACKSPACE to cancel.
		else if (e.getKeyChar() == KeyEvent.VK_SPACE) cycleBlockType(); // SPACE to cycle block types.
		else if (Character.toUpperCase(e.getKeyChar()) == KeyEvent.VK_L) { // L to load a level.
			try {
				File file = selectLoadFile();
				if (file != null) resetGame();

				loadLevel(file);
			} catch (IOException ignored) {}
		}
	}

	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void keyPressed(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}
}
