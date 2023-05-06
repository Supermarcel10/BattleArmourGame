package game;

import city.cs.engine.*;
import game.IO.AM;
import game.IO.Config;
import game.IO.DatabaseHandler;
import game.main.GameState;
import game.main.SoundHandler;
import game.objects.block.Block;
import game.objects.block.BlockType;
import game.objects.shot.Shot;
import game.objects.tank.Enemy;
import game.objects.pickup.Pickup;
import game.objects.tank.Player;
import game.objects.tank.Spawn;
import game.IO.Listener;
import game.objects.tank.TankType;
import game.window.WindowHandler;
import game.window.WindowMenu;
import game.window.WindowPlay;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import static game.IO.Config.resolution;
import static game.IO.LoadLevel.loadLevel;
import static game.window.WindowHandler.view;
import static game.window.WindowPlay.updateScore;
import static java.lang.Thread.sleep;


public class MainGame {
	public static World world;
	public static Player[] player = new Player[2];
	public static Block[][] blocks;
	public static int[][] blockCosts;
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

	public static int numOfPlayers = 0;

	public static File currentLevel;
	public static int score = 0, postUpdateScore = 0;
	public static int kills = 0;
	public static int brokenBlocks = 0;

	public static Thread spawnThread = new Thread(MainGame::enemySpawn);
	public static GameState gameState = GameState.NONE;


	public static void main(String[] args) {
		// Add a new exit hook to close the database connection if the connection exists.
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (DatabaseHandler.connection != null) DatabaseHandler.disconnectFromDB();
		}));

		world = new World(Config.fps);

		// Connect to DB & create table if not exists.
		DatabaseHandler.connectToDB();
		DatabaseHandler.createTable();

		// Create the window and main menu.
		WindowHandler.createWindow();

		// Disable the gravity and change background color.
		world.setGravity(0);
		view.setBackground(Color.decode("#fcf8de"));

		// Add Keyboard & Mouse listeners.
		Listener listener = new Listener();
		view.addMouseListener(listener);
		view.addKeyListener(listener);
		world.addStepListener(listener);

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

	public static void loadGame(File level) {
		currentLevel = level;

		// Set the grid size and scaled grid size.
		scaledGridSize = (((27 * scaleFactor) / gridSize) / scaleFactor);
		blocks = new Block[gridSize][gridSize];
		blockCosts = new int[gridSize][gridSize];
		Arrays.stream(blockCosts).forEach(a -> Arrays.fill(a, 1));

		// Play the game started sound.
		soundHandler.playBackgroundMusic();
		soundHandler.play(AM.miscSound.get("gameStart"));

		// Load the level.
		try {
			if (!loadLevel(level)) throw new ExceptionInInitializerError("Failed to initialise level!");
			WindowPlay.createGameOverlay();

			// Start world simulation
			world.start();

			// Spawn players.
			for (int i = 0; i < numOfPlayers; i++) {
				new Spawn(TankType.PLAYER, playerSpawnPos[i]);
			}

			// Spawn enemies progressively.
			if (spawnThread.getState() == Thread.State.TERMINATED) spawnThread = new Thread(MainGame::enemySpawn);
			spawnThread.start();

			gameState = GameState.GAME;
		} catch (Exception ignored) {
			System.out.println("Failed to load level!");
			WindowPlay.hideGameOverlay();
			WindowMenu.showMenu();
		}
	}

	public static void outputDebug() {
		System.out.println("");

		for (int y = gridSize - 1; y >= 0; y--) {
			for (int x = 0; x < gridSize; x++) {
				Block b = blocks[x][y];

				if (b != null) {
					System.out.print(b.type.toString().toCharArray()[0] + "  ");
				} else {
					System.out.print("   ");
				}
			}
			System.out.print("\n");
		}

		System.out.println("");

		for (int y = gridSize - 1; y >= 0; y--) {
			for (int x = 0; x < gridSize; x++) {
				Block b = blocks[x][y];
				int cost = blockCosts[x][y];

				if (cost == -1) {
					System.out.print("N  ");
				} else {
					System.out.print(cost + "  ");
				}
			}
			System.out.print("\n");
		}
	}

	public static void resetGame() {
		// TODO: Rewrite this method.
		if (spawnThread.isAlive()) spawnThread.interrupt();

		// Remove all blocks.
		for (Block[] block : blocks) {
			for (Block b : block) {
				if (b != null) {
					b.destroy();
				}
			}
		}
		blocks = new Block[gridSize][gridSize];

		// Remove all pickups.
		for (Pickup pickup : pickups) {
			if (pickup != null) {
				pickup.destroy();
			}
		}

		// Reset all players
		for (Player player : player) {
			if (player != null) {
				player.destroy();
			}
		}
		player = new Player[2];

		// Remove all shots.
		Iterator<Shot> iterator = shots.iterator();
		while (iterator.hasNext()) {
			Shot s = iterator.next();
			s.destroy();
		}

		// Remove all enemies.
		for (Enemy enemy : enemies) {
			if (enemy != null) {
				enemy.destroy();
			}
		}
		enemies.clear();

		// Reset all variables.
		basePos = null;
		numOfPlayers = 0;
		score = 0; postUpdateScore = 0;
		kills = 0;
		brokenBlocks = 0;
		updateScore();

		// Reset all lists.
		enemySpawnPos = new ArrayList<>();
		playerSpawnPos = new Vec2[2];
	}

	public static void makeMapBorder() {
		for (int i = 0; i < gridSize; i++) {
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, -hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize, -hGridSize + i), true);
			new Block(BlockType.EDGE, new Vec2(hGridSize, -hGridSize + i), true);
		}
	}

	private static void enemySpawn() {
		while (true) {
			if (enemies.size() < 3) spawnEnemy();

			try {
				sleep(1000 + (int) (Math.random() * 4000));
			} catch (InterruptedException ignored) {
				break;
			}
		}
	}

	private static void spawnEnemy() {
		try {
			new Spawn(determineSpawnType(), generateSpawnPos());
		} catch (NullPointerException ignored) {
			spawnEnemy();
		} catch (StackOverflowError ignored) {}
	}

	private static TankType determineSpawnType() {
		int totalSpawnPercentage = 100;

		// Define the initial spawn chances.
		double basicChance = 80.0;
		double fastChance = 10.0;
		double heavyChance = 5.0;
		double explodingChance = 5.0;

		// Define the desired proportional increase percentages at max score.
		double basicIncrease = 20.0;
		double fastIncrease = 22.5;
		double heavyIncrease = 22.5;
		double explodingIncrease = 22.5;

		float scoreLimit = 25_000f / numOfPlayers;

		// Calculate the available percentage increase based on the score
		double availableIncrease = Math.min(score / scoreLimit * 17.5, 17.5);

		// Distribute the available increase proportionally among the three enemy types
		double increasePerType = availableIncrease / 3;

		// Adjust the spawn chances accordingly
		basicChance += basicIncrease + increasePerType;
		fastChance += fastIncrease + increasePerType;
		heavyChance += heavyIncrease + increasePerType;
		explodingChance += explodingIncrease;

		// Ensure the total spawn percentage remains unchanged (excluding EXPLODING)
		double totalAdjustedPercentage = basicChance + fastChance + heavyChance + explodingChance;
		double adjustmentFactor = totalSpawnPercentage / totalAdjustedPercentage;

		// Apply the adjustment factor to keep the total spawn percentage at 100%
		basicChance *= adjustmentFactor;
		fastChance *= adjustmentFactor;
		heavyChance *= adjustmentFactor;
		explodingChance *= adjustmentFactor;

		// Return the appropriate TankType based on the adjusted spawn chances
		double randomValue = Math.random() * totalSpawnPercentage;

		if (randomValue < basicChance) {
			return TankType.BASIC;
		} else if (randomValue < basicChance + fastChance) {
			return TankType.FAST;
		} else if (randomValue < basicChance + fastChance + heavyChance) {
			return TankType.HEAVY;
		} else if (randomValue < basicChance + fastChance + heavyChance + explodingChance) {
			return TankType.EXPLODING;
		} else {
			return TankType.BASIC;
		}
	}

	private static Vec2 generateSpawnPos() {
		Vec2 pos = enemySpawnPos.get((int) (Math.random() * enemySpawnPos.size()));

		for (Spawn spawn : spawners) {
			if (spawn != null && isWithinDistance(spawn.getPosition(), pos, 1)) return generateSpawnPos();
		}

		for (Player player : player) {
			if (player != null && isWithinDistance(player.getPosition(), pos, 2)) return generateSpawnPos();
		}

		for (Enemy enemy : enemies) {
			if (enemy != null && isWithinDistance(enemy.getPosition(), pos, 2)) return generateSpawnPos();
		}
		return pos;
	}

	public static boolean isWithinDistance(@NotNull Vec2 vec1, @NotNull Vec2 vec2, float distance) {
		float dx = Math.abs(vec1.x - vec2.x);
		float dy = Math.abs(vec1.y - vec2.y);
		return dx <= distance && dy <= distance;
	}

	/**
	 * Converts the enum name to camel case.
	 * @param s The enum name.
	 * @return The enum name in camel case.
	 */
	public static @NotNull String toCamelCase(@NotNull String s) {
		String[] parts = s.split("_");
		StringBuilder out = new StringBuilder();
		out.append(parts[0].toLowerCase());

		for (int i = 1; i < parts.length; i++) {
			out.append(parts[i].substring(0, 1).toUpperCase());
			out.append(parts[i].substring(1).toLowerCase());
		}

		return out.toString();
	}
}
