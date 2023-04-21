package game.main;

import city.cs.engine.*;
import game.input.Config;
import game.objects.Block;
import game.objects.Enemy;
import game.prefab.*;
import game.input.Listener;
import game.window.GameState;
import game.window.WindowHandler;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

import static game.input.Config.resolution;
import static game.main.LoadLevel.loadLevel;
import static game.window.WindowHandler.view;
import static java.lang.Thread.sleep;


public class Game {
	public static World world;
	public static Player[] player = new Player[2];
	public static Block[][] blocks;
	public static HashSet<Spawn> spawners = new HashSet<>();
	public static HashSet<Enemy> enemies = new HashSet<>();
	public static HashSet<Shot> shots = new HashSet<>();

	public static SoundHandler soundHandler = new SoundHandler();

	public static Vec2 basePos;

	public static float scaleFactor = resolution.x / 1920;
	public static int gridSize = 15;
	public static final int hGridSize = gridSize / 2;
	public static float scaledGridSize;

	public static int score = 0, postUpdateScore = 0;
	public static int kills = 0;
	public static int brokenBlocks = 0;

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
		loadGame();

		// Add Keyboard & Mouse listeners.
		Listener listener = new Listener();
		view.addMouseListener(listener);
		view.addKeyListener(listener);
		world.addStepListener(listener);

		// Start world simulation
		world.start();

		// Play background music.
		SoundHandler.playBackgroundMusic();

		// Spawn enemies progressively.
//		new Thread(Game::enemySpawn).start();

		// Create a thread to print the FPS.
		Thread thread = new Thread(() -> {
			while (true) {
				System.out.println(world.getSimulationSettings().getAveragedFPS());
				// Sleep for 1 second.
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
//		thread.start();
	}

	public static void loadGame() {
		gameState = GameState.LOADING;
		WindowHandler.createGameOverlay();

		scaledGridSize = (((27 * scaleFactor) / gridSize) / scaleFactor);
		blocks = new Block[gridSize][gridSize];

		try {
			if (!loadLevel("C:\\Users\\Marcel\\IdeaProjects\\javaproject2023-Supermarcel10\\src\\main\\resources\\levels\\1.level")) {
				throw new ExceptionInInitializerError("Failed to initialise level!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Always make a border around the map.
		for (int i = 0; i < gridSize; i++) {
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, -hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize + i, hGridSize), true);
			new Block(BlockType.EDGE, new Vec2(-hGridSize, -hGridSize + i), true);
			new Block(BlockType.EDGE, new Vec2(hGridSize, -hGridSize + i), true);
		}

//		 Make a character (with an overlaid image).
		new Spawn(TankType.PLAYER, new Vec2(-1, 0));
		new Spawn(TankType.PLAYER, new Vec2(1, 0));

		// Make a few enemies for testing.
		new Spawn(TankType.EXPLODING, new Vec2(-6, 6));
		new Spawn(TankType.BASIC, new Vec2(-2, 6));
		new Spawn(TankType.HEAVY, new Vec2(2, 6));
		new Spawn(TankType.FAST, new Vec2(6, 6));

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
		Iterator<Enemy> iterator = enemies.iterator();
		while (iterator.hasNext()) {
			Enemy enemy = iterator.next();
			if (enemy != null) {
				enemy.destroy();
				iterator.remove();
			}
		}

		// TODO: Remove all pickups.

		// Reset all players
		for (Player player : player) {
			if (player != null) {
				player.destroy();
			}
		}
	}

	private static void enemySpawn() {
		// TODO: Make sure newly spawned characters don't spawn inside of blocks or other characters.
		while (true) {
			if (enemies.size() < 3) {
				new Spawn(TankType.BASIC, new Vec2((int) (Math.random() * 12) - 6, 6));
			}

			try {
				sleep(5000 + (int) (Math.random() * 5000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
