package game.IO;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import game.MainGame;
import game.main.GameState;
import game.main.LevelCreator;
import game.objects.block.BlockType;
import game.window.WindowDeath;
import game.window.WindowHandler;
import game.objects.tank.Enemy;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static game.IO.LoadLevel.loadLevel;
import static game.MainGame.*;
import static game.window.WindowCommons.selectLoadFile;
import static game.window.WindowHandler.view;


/**
 * The listener class for all input devices and events.
 */
public class Listener implements KeyListener, MouseListener, StepListener {
	/**
	 * Called when the game view is hovered.
	 * @param e The event to be processed.
	 */
	@Override public void mouseEntered(MouseEvent e) {
		view.requestFocus();
	}

	/**
	 * Called when a mouse interaction occurs via mouse button down.
	 * Used to handle level creation.
	 * @param e The event to be processed.
	 */
	@Override public void mousePressed(MouseEvent e) {
		if (gameState == GameState.EDITOR) {
			// Translate the mouse to start from the center of the screen.
			e.translatePoint(-view.getWidth() / 2, -view.getHeight() / 2);

			Vec2 pos = new Vec2(
					Math.round(e.getPoint().x / ((float) view.getWidth() / gridSize)),
					-Math.round(e.getPoint().y / ((float) view.getHeight() / gridSize))
			);

			if (e.getButton() == MouseEvent.BUTTON1) LevelCreator.placeBlock(pos);
			else if (e.getButton() == MouseEvent.BUTTON3) LevelCreator.cycleBlockType();
		}
	}

	/**
	 * Called when a key is pressed and released.
	 * @param e The event to be processed.
	 */
	@Override public void keyTyped(KeyEvent e) {
		if (gameState == GameState.EDITOR) {
			if (e.getKeyChar() == KeyEvent.VK_ESCAPE) LevelCreator.finish(); // ESC to exit.
			else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) LevelCreator.exit(); // BACKSPACE to cancel.
			else if (e.getKeyChar() == KeyEvent.VK_SPACE) LevelCreator.cycleBlockType(); // SPACE to cycle block types.
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
								LevelCreator.playerPos[LevelCreator.changedPlayerPos] = new Vec2(x - hGridSize, y - hGridSize);
								LevelCreator.changedPlayerPos = (LevelCreator.changedPlayerPos + 1) % 2;
							} else if (blocks[x][y].type == BlockType.ENEMY_SPAWN) {
								LevelCreator.enemyPos.add(new Vec2(x - hGridSize, y - hGridSize));
							} else if (blocks[x][y].type == BlockType.BASE) {
								LevelCreator.basePos = new Vec2(x - hGridSize, y - hGridSize);
							}
						}
					}
				} catch (IOException ignored) {}
			}
		}
	}

	/**
	 * Called when a key is pressed.
	 * Used to handle player movement and shooting.
	 * @param e The event to be processed.
	 */
	@Override
	public void keyPressed(@NotNull KeyEvent e) {
		if (gameState == GameState.GAME) {
			if (player[0] != null) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_A -> player[0].moveDirection = new Vec2(-1, 0);
					case KeyEvent.VK_D -> player[0].moveDirection = new Vec2(1, 0);
					case KeyEvent.VK_W -> player[0].moveDirection = new Vec2(0, 1);
					case KeyEvent.VK_S -> player[0].moveDirection = new Vec2(0, -1);
					case KeyEvent.VK_SPACE -> player[0].shoot();
				}
			}

			if (player[1] != null) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT -> player[1].moveDirection = new Vec2(-1, 0);
					case KeyEvent.VK_RIGHT -> player[1].moveDirection = new Vec2(1, 0);
					case KeyEvent.VK_UP -> player[1].moveDirection = new Vec2(0, 1);
					case KeyEvent.VK_DOWN -> player[1].moveDirection = new Vec2(0, -1);
					case KeyEvent.VK_ENTER -> player[1].shoot();
				}
			}
		}
	}

	/**
	 * Called when a key is released.
	 * Used to stop player movement.
	 * @param e The event to be processed.
	 */
	@Override
	public void keyReleased(@NotNull KeyEvent e) {
		if (player[0] != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A, KeyEvent.VK_D -> player[0].moveDirection = new Vec2(0, player[0].moveDirection.y);
				case KeyEvent.VK_W, KeyEvent.VK_S -> player[0].moveDirection = new Vec2(player[0].moveDirection.x, 0);
			}
		}

		if (player[1] != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> player[1].moveDirection = new Vec2(0, player[1].moveDirection.y);
				case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> player[1].moveDirection = new Vec2(player[0].moveDirection.x, 0);
			}
		}
	}

	/**
	 * Called before each frame step.
	 * Used to update all game objects.
	 */
	@Override
	public void preStep(StepEvent ignored) {
		if (player[0] != null && !Objects.equals(player[0].moveDirection, new Vec2(0, 0))) player[0].update();
		if (player[1] != null && !Objects.equals(player[1].moveDirection, new Vec2(0, 0))) player[1].update();

		for (Enemy enemy : enemies) enemy.update();

		if ((MainGame.basePos != null && blocks[(int) MainGame.basePos.x][(int) MainGame.basePos.y] != null &&
				blocks[(int) MainGame.basePos.x][(int) MainGame.basePos.y].health <= 0) ||
				((player[0] != null && player[0].health <= 0) && (player[1] != null && player[1].health <= 0))) {
			WindowDeath.createDeathMenu();
			world.stop();
		}

		if (postUpdateScore != score) {
			WindowHandler.updateScore();
			postUpdateScore = score;
		}
	}

	@Override public void postStep(StepEvent stepEvent) {}
	@Override public void mouseClicked(@NotNull MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}
