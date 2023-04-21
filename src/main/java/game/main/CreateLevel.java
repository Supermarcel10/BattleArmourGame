package game.main;

import game.objects.block.Block;
import game.objects.block.BlockType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static game.MainGame.*;
import static game.IO.LoadLevel.loadLevel;
import static game.IO.SaveLevel.saveLevel;
import static game.window.WindowBuild.*;
import static game.window.WindowCommons.selectLoadFile;
import static game.window.WindowHandler.view;


public class CreateLevel implements MouseListener, KeyListener {
	private static BlockType currentBlockType = BlockType.BRICK;
	private static Vec2 basePos;
	private static final Vec2[] playerPos = new Vec2[2];
	private static int changedPlayerPos = 0;
	private static final List<Vec2> enemyPos = new ArrayList<>();


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

	private void placeBlock(Vec2 pos) {
		removeIfExists(pos);

		if (currentBlockType == BlockType.NONE) return;
		else if (currentBlockType == BlockType.ENEMY_SPAWN) {
			enemyPos.add(pos);
		} else if (currentBlockType == BlockType.PLAYER_SPAWN) {
			// Remove the oldest spawn.
			removeIfExists(playerPos[changedPlayerPos]);

			// Set the new spawn location.
			playerPos[changedPlayerPos] = pos;

			// Change the player spawn that will be changed next.
			changedPlayerPos = (changedPlayerPos + 1) % 2;
		} else if (currentBlockType == BlockType.BASE) {
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
		updateBlockPlacement(currentBlockType.toString().replace("_", " "));
	}

	public void finish() {
		// Check if the map is valid.
		if (playerPos[0] == null || playerPos[1] == null) {
			updateInformation("Both player spawns must be present!");
			return;
		}

		if (enemyPos.size() == 0) {
			updateInformation("No enemy spawns found!");
			return;
		}

		// Let the user select save location and attempt saving to it.
		try {
			saveLevel(selectSaveLocation());
		} catch (Exception ignored) {
			updateInformation("Failed to save level!");
			return;
		}

		// If successful, exit the level editor.
		exit();
	}

	private void removeIfExists(Vec2 pos) {
		if (pos == null) return;

		// If the block is a base, remove the base position.
		if (blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize] != null) {
			if (blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize].type == BlockType.BASE) basePos = null;
			if (blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize].type == BlockType.ENEMY_SPAWN) enemyPos.remove(pos);
			if (blocks[(int) pos.x + hGridSize][(int) pos.y + hGridSize].type == BlockType.PLAYER_SPAWN) {
				if (playerPos[0] != null && playerPos[0].equals(pos)) changedPlayerPos = 0;
				if (playerPos[1] != null && playerPos[1].equals(pos)) changedPlayerPos = 1;

				playerPos[changedPlayerPos] = null;
			}
		}

		Block.removeIfExists(pos);
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

				loadLevel(file, true);

				// Parse the blocks to find the player and enemy spawns.
				for (int x = 0; x < gridSize; x++) {
					for (int y = 0; y < gridSize; y++) {
						if (blocks[x][y] == null) continue;

						if (blocks[x][y].type == BlockType.PLAYER_SPAWN) {
							playerPos[changedPlayerPos] = new Vec2(x - hGridSize, y - hGridSize);
							changedPlayerPos = (changedPlayerPos + 1) % 2;
						} else if (blocks[x][y].type == BlockType.ENEMY_SPAWN) {
							enemyPos.add(new Vec2(x - hGridSize, y - hGridSize));
						} else if (blocks[x][y].type == BlockType.BASE) {
							basePos = new Vec2(x - hGridSize, y - hGridSize);
						}
					}
				}
			} catch (IOException ignored) {}
		}
	}

	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void keyPressed(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}
}
