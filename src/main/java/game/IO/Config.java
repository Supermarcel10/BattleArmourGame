package game.IO;

import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;


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

	public static HashMap<String, String> image = new HashMap<>();
	public static HashMap<String, String> tankSound = new HashMap<>();
	public static HashMap<String, String> blockSound = new HashMap<>();
	public static HashMap<String, String> music = new HashMap<>();
	public static HashMap<String, String> font = new HashMap<>();

	static {
		addAllToHashMap(image, new File(CD + "img/")); // image
		addAllToHashMap(tankSound, new File(CD + "sound/tank/")); // tankSound
		addAllToHashMap(blockSound, new File(CD + "sound/blocks/")); // blockSound
		addAllToHashMap(music, new File(CD + "music/")); // music
		addAllToHashMap(font, new File(CD + "font/")); // font
	}

	private static void addAllToHashMap(HashMap hashMap, @NotNull File file) {
		if (file.isDirectory()) {
			for (File f : Objects.requireNonNull(file.listFiles())) {
				addAllToHashMap(hashMap, f);
			}
		} else {
			if (file.getName().split("\\.").length > 2 && Objects.equals(file.getName().split("\\.")[2], "kra")) return;
			hashMap.put(file.getName().split("\\.")[0], file.getPath());
		}
	}
}
