package game.IO;

import org.jbox2d.common.Vec2;

import java.util.HashMap;


/**
 * Config class containing all user settings.
 */
public class Config {
	private static final String CD = System.getProperty("user.dir") + "/src/main/resources/";
	public static final boolean DEBUG = true;
	public static final boolean DEBUG_DRAW = false;

	public static float soundVolume = 0.2f, musicVolume = 0.4f;

	public static boolean fullscreen = false;
//	public static Vec2 resolution = new Vec2(1280, 720);
//	public static Vec2 resolution = new Vec2(1920, 1080);
	public static Vec2 resolution = new Vec2(2560, 1440);
	public static int fps = 60;
	public static String title = "Battle Armour";
	public static HashMap<String, Character> key_binding = new HashMap<>(){{
		put("player1-UP", 'w');
		put("player1-LEFT", 'a');
		put("player1-DOWN", 's');
		put("player1-RIGHT", 'd');
		put("player1-SHOOT", ' ');

		put("player2-UP", '↑');
		put("player2-LEFT", '←');
		put("player2-DOWN", '↓');
		put("player2-RIGHT", '→');
		put("player2-SHOOT", '\n');
	}};
}
