package game.input;

import org.jbox2d.common.Vec2;

import java.util.HashMap;


public class Config {
	private static final String CD = System.getProperty("user.dir") + "/src/main/resources/";
	public static final boolean DEBUG = false;

	public static boolean fullscreen = false;
//	public static Vec2 resolution = new Vec2(1280, 720);
//	public static Vec2 resolution = new Vec2(1920, 1080);
	public static Vec2 resolution = new Vec2(2560, 1440);
	public static boolean fpsLock = false;
	public static int fps = 144;
	public static String title = "Battle Armour";
	public static HashMap<Character, String> key_binding = new HashMap<>(){{
		put('w', "player1-UP");
		put('a', "player1-LEFT");
		put('s', "player1-DOWN");
		put('d', "player1-RIGHT");
		put('↑', "player2-UP");
		put('←', "player2-LEFT");
		put('↓', "player2-DOWN");
		put('→', "player2-RIGHT");
	}};

	public static HashMap<String, String> image = new HashMap<>() {{
		put("edge", CD + "img/blocks/edge.png");
		put("wall", CD + "img/blocks/brick.png");
		put("base", CD + "img/blocks/base.png");

		put("player", CD + "img/player/player.gif");
		put("basicEnemy", CD + "img/enemy/basicEnemy.gif");
		put("heavyEnemy", CD + "img/enemy/heavyEnemy.gif");
		put("fastEnemy", CD + "img/enemy/fastEnemy.gif");
		put("explodingEnemy", CD + "img/enemy/explosiveEnemy.gif");
	}};

	public static HashMap<String, String> sound = new HashMap<>() {{
//		put("playerDeath", CD + "sound/playerDeath.wav");
//		put("enemyDeath", CD + "sound/enemyDeath.wav");
//		put("explosion", CD + "sound/explosion.wav");
	}};

	public static HashMap<String, String> music = new HashMap<>() {{
//		put("menu", CD + "music/menu.mp3");
//		put("game", CD + "music/game.mp3");
	}};

	public static HashMap<String, String> font = new HashMap<>() {{
		put("default", CD + "font/PressStart2P.ttf");
	}};
}
