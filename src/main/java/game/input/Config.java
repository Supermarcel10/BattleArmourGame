package game.input;

import org.jbox2d.common.Vec2;

import java.util.HashMap;


public class Config {
	private static final String CD = System.getProperty("user.dir");
	public static final boolean DEBUG = false;

	public static boolean fullscreen = false;
	public static Vec2 resolution = new Vec2(1280, 720);
//	public static Vec2 resolution = new Vec2(1920, 1080);
//	public static Vec2 resolution = new Vec2(2560, 1440);
	public static boolean fpsLock = false;
	public static int fps = 144;
	public static String title = "City Game";
	public static HashMap<Character, String> key_binding = new HashMap<>(){{
		put('w', "player1-UP");
		put('a', "player1-LEFT");
		put('s', "player1-DOWN");
		put('d', "player1-RIGHT");
	}};

	public static HashMap<String, String> image = new HashMap<>() {{
		put("edge", CD + "\\src\\main\\resources\\img\\blocks\\edge.png");
		put("wall", CD + "\\src\\main\\resources\\img\\blocks\\brick.png");

		put("player", CD + "\\src\\main\\resources\\img\\player\\player.gif");
	}};
}
