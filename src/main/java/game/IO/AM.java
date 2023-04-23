package game.IO;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;


public class AM {
	private static final String CD = System.getProperty("user.dir") + "/src/main/resources/";

	public static HashMap<String, String> image = new HashMap<>();
	public static HashMap<String, String> tankSound = new HashMap<>();
	public static HashMap<String, String> blockSound = new HashMap<>();
	public static HashMap<String, String> music = new HashMap<>();
	public static HashMap<String, String> font = new HashMap<>();
	public static HashMap<String, String> level = new HashMap<>();

	static {
		addAllToHashMap(image, new File(CD + "img/")); // image
		addAllToHashMap(tankSound, new File(CD + "sound/tank/")); // tankSound
		addAllToHashMap(blockSound, new File(CD + "sound/blocks/")); // blockSound
		addAllToHashMap(music, new File(CD + "music/")); // music
		addAllToHashMap(font, new File(CD + "font/")); // font
		addAllToHashMap(level, new File(CD + "levels/")); // level
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
