package game.IO;

import org.jbox2d.common.Vec2;

import java.util.HashMap;


public class Config {
	private static final String CD = System.getProperty("user.dir") + "/src/main/resources/";
	public static final boolean DEBUG = true;
	public static final boolean DEBUG_DRAW = false;

	public static boolean fullscreen = false;
//	public static Vec2 resolution = new Vec2(1280, 720);
//	public static Vec2 resolution = new Vec2(1920, 1080);
	public static Vec2 resolution = new Vec2(2560, 1440);
	public static boolean fpsLock = false;
	public static int fps = 60;
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
}
