package game.input;

import org.jbox2d.common.Vec2;

import java.util.HashMap;


public class Config {
	public static final boolean DEBUG = false;
	public static boolean fullscreen = false;
//	public static Vec2 resolution = new Vec2(1920, 1080);
	public static Vec2 resolution = new Vec2(2560, 1440);
	public static boolean fpsLock = false;
	public static int fps = 144;
	public static String title = "City Game";
	public static HashMap<Character, String> key_binding = new HashMap<>(){{
		put('w', "player1-UP");
		put('a', "player1-LEFT");
		put('s', "player1-DOWN");
		put('d', "player1-RIGHT");
	}};
}
