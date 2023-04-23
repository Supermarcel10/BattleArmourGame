package game.main;

import game.objects.block.Block;
import game.objects.block.BlockType;
import game.window.WindowMenu;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

import static game.MainGame.*;
import static game.IO.SaveLevel.saveLevel;
import static game.window.WindowBuild.*;


public class LevelCreator {
	public static BlockType currentBlockType = BlockType.BRICK;
	public static Vec2 basePos;
	public static final Vec2[] playerPos = new Vec2[2];
	public static int changedPlayerPos = 0;
	public static final List<Vec2> enemyPos = new ArrayList<>();

	public static void createLevel() {
		gameState = GameState.EDITOR;

		// Start world simulation
		world.start();

		scaledGridSize = (((27 * scaleFactor) / gridSize) / scaleFactor);
		blocks = new Block[gridSize][gridSize];

		// Create the overlay.
		createBuildOverlay();

		makeMapBorder();
	}

	public static void placeBlock(Vec2 pos) {
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

	public static void cycleBlockType() {
		// Cycle through the block types.
		currentBlockType = BlockType.values()[(currentBlockType.ordinal() + 1) % BlockType.values().length];
		updateBlockPlacement(currentBlockType.toString().replace("_", " "));
	}

	public static void finish() {
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

		// If successful, stop world simulation & exit the level editor.
		world.stop();
		gameState = GameState.MENU;
		exit();
	}

	private static void removeIfExists(Vec2 pos) {
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

	public static void exit() {
		// Remove the overlay.
		removeBuildOverlay();

		// Reset the game and go back to the menu.
		resetGame();
		WindowMenu.showMenu();
	}
}
