package game;

import city.cs.engine.*;
import game.IO.Config;
import game.main.GameState;
import game.main.SoundHandler;
import game.objects.block.Block;
import game.objects.shot.Shot;
import game.objects.tank.Enemy;
import game.objects.pickup.Pickup;
import game.objects.block.BlockType;
import game.objects.tank.Player;
import game.objects.tank.Spawn;
import game.objects.tank.TankType;
import game.IO.Listener;
import game.window.WindowHandler;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.util.*;
import java.util.List;

import static game.IO.Config.resolution;
import static game.IO.LoadLevel.loadLevel;
import static game.window.WindowHandler.view;
import static java.lang.Thread.sleep;


public class MainGame {
	public static World world;
	public static Player[] player = new Player[2];
	public static Block[][] blocks;
	public static HashSet<Spawn> spawners = new HashSet<>();
	public static HashSet<Enemy> enemies = new HashSet<>();
	public static HashSet<Pickup> pickups = new HashSet<>();
	public static HashSet<Shot> shots = new HashSet<>();

	public static SoundHandler soundHandler = new SoundHandler();

	public static Vec2 basePos;
	public static Vec2[] playerSpawnPos = new Vec2[2];
	public static List<Vec2> enemySpawnPos = new ArrayList<>();

	public static float scaleFactor = resolution.x / 1920;
	public static int gridSize = 15;
	public static final int hGridSize = gridSize / 2;
	public static float scaledGridSize;

	public static int score = 0, postUpdateScore = 0;
	public static int kills = 0;
	public static int brokenBlocks = 0;

	public static Thread spawnThread = new Thread(MainGame::enemySpawn);

	public static GameState gameState = GameState.NONE;


	public static void main(String[] args) {
		world = new World(Config.fps);

		// Create the window and main menu.
		WindowHandler.createWindow(world);

		while (WindowHandler.inMenu) {
			// TODO: Make menu handling.
			WindowHandler.inMenu = false;
		}

		// Disable the gravity and change background color.
		world.setGravity(0);
		view.setBackground(Color.decode("#fcf8de"));

//		new CreateLevel();
//		loadGame();

		// Add Keyboard & Mouse listeners.
		Listener listener = new Listener();
		view.addMouseListener(listener);
		view.addKeyListener(listener);
		world.addStepListener(listener);

		// Start world simulation
		world.start();

		// Play background music.
		SoundHandler.playBackgroundMusic();

		// Create a thread to print the FPS.
//		Thread thread = new Thread(() -> {
//			while (true) {
//				System.out.println(world.getSimulationSettings().getAveragedFPS());
//				// Sleep for 1 second.
//				try {
//					sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		thread.start();
	}

	public static void loadGame() {
		gameState = GameState.LOADING;
		WindowHandler.createGameOverlay();

		scaledGridSize = (((27 * scaleFactor) / gridSize) / scaleFactor);
		blocks = new Block[gridSize][gridSize];

		try {
			if (!loadLevel("C:\\Users\\Marcel\\IdeaProjects\\javaproject2023-Supermarcel10\\src\\main\\resources\\levels\\2.level")) {
				throw new ExceptionInInitializerError("Failed to initialise level!");
			}
		} catch (Exception ignored) {}

		// Always make a border around the map.
		for (int i = 0; i < gridSize; i++) {
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, -hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize, -hGridSize + i), true);
			new Block(BlockType.EDGE, new Vec2(hGridSize, -hGridSize + i), true);
		}

		// Spawn players.
		new Spawn(TankType.PLAYER, playerSpawnPos[0]);
		new Spawn(TankType.PLAYER, playerSpawnPos[1]);

		// Spawn enemies progressively.
		spawnThread.start();

		gameState = GameState.PLAYING;
	}

	public static void resetGame() {
		// Reset all blocks
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j] != null) {
					blocks[i][j].destroy();
					blocks[i][j] = null;
				}
			}
		}

		// Remove all enemies.
		Iterator<Enemy> iterEnemy = enemies.iterator();
		while (iterEnemy.hasNext()) {
			Enemy enemy = iterEnemy.next();
			if (enemy != null) {
				enemy.destroy();
				iterEnemy.remove();
			}
		}

		// Remove all spawners.
		Iterator<Spawn> iterSpawners = spawners.iterator();
		while (iterSpawners.hasNext()) {
			Spawn spawn = iterSpawners.next();
			if (spawn != null) {
				spawn.destroy();
				iterSpawners.remove();
			}
		}

		// Remove all pickups.
		Iterator<Pickup> iterPickups = pickups.iterator();
		while (iterPickups.hasNext()) {
			Pickup pickup = iterPickups.next();
			if (pickup != null) {
				pickup.destroy();
				iterPickups.remove();
			}
		}

		// Reset all players
		for (Player player : player) {
			if (player != null) {
				player.destroy();
			}
		}
	}

	private static void enemySpawn() {
		// TODO: Randomise enemy type and make progression harder.
		while (true) {
			if (enemies.size() < 3) {
				Vec2 pos = enemySpawnPos.get((int) (Math.random() * enemySpawnPos.size()));
				new Spawn(TankType.BASIC, pos);
			}

			try {
				sleep(5000 + (int) (Math.random() * 5000));
			} catch (InterruptedException ignored) {}
		}
	}
}
