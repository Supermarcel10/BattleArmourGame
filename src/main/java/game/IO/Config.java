package game.IO;

import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.ApiStatus;

import java.awt.event.KeyEvent;
import java.util.HashMap;


/**
 * Config class containing all user settings.
 */
public class Config {
	public static float soundVolume = 0.2f, musicVolume = 0.4f;

	public @ApiStatus.Experimental static boolean fullscreen = false;
	public static Vec2 resolution = new Vec2(1280, 720);
//	public static Vec2 resolution = new Vec2(1920, 1080);
//	public static Vec2 resolution = new Vec2(2560, 1440);

	public static int fps = 60;

	public static HashMap<String, Integer> key_binding = new HashMap<>(){{
		put("player1-UP", KeyEvent.VK_W);
		put("player1-LEFT", KeyEvent.VK_A);
		put("player1-DOWN", KeyEvent.VK_S);
		put("player1-RIGHT", KeyEvent.VK_D);
		put("player1-SHOOT", KeyEvent.VK_SPACE);

		put("player2-UP", KeyEvent.VK_UP);
		put("player2-LEFT", KeyEvent.VK_LEFT);
		put("player2-DOWN", KeyEvent.VK_DOWN);
		put("player2-RIGHT", KeyEvent.VK_RIGHT);
		put("player2-SHOOT", KeyEvent.VK_ENTER);
	}};
}
