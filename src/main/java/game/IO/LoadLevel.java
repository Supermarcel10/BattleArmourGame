package game.IO;

import game.objects.block.Block;
import game.objects.block.BlockType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static game.MainGame.enemySpawnPos;
import static game.MainGame.playerSpawnPos;


public class LoadLevel {
	public static boolean loadLevel(File file, boolean force) throws IOException {
		if (file == null) return false;

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		while ((line = br.readLine()) != null) {
			switch (line.charAt(0)) {
				case 'i' -> handleInfoParsing(line);
				case 'a' -> handleAudioParsing(line);
				case 'b' -> handleBlockPlacement(line, force);
				case 'E' -> { // End of level
					if (line.equals("END")) {
						br.close();
						return true;
					}
				}
			}
		}

		// If END is not found, return failure to load.
		br.close();
		return false;
	}

	public static boolean loadLevel(File file) throws IOException {
		return loadLevel(file, false);
	}

	public static boolean loadLevel(String path, boolean force) throws IOException {
		return loadLevel(new File(path), force);
	}

	public static boolean loadLevel(String path) throws IOException {
		return loadLevel(new File(path), false);
	}

	private static void handleInfoParsing(@NotNull String line) {
		// TODO: Implement custom level information
		System.out.println("Level information: " + line.substring(2));
	}

	private static void handleAudioParsing(@NotNull String line) {
		// TODO: Implement custom audio information
		System.out.println("Audio information: " + line.substring(2));
	}

	private static void handleBlockPlacement(String line, boolean force) {
		String[] parsedData = parseData(line);
		int x = Integer.parseInt(parsedData[1]);
		int y = Integer.parseInt(parsedData[2]);

		BlockType type = BlockType.valueOf(parsedData[0]);

		if (!force) {
			if (type == BlockType.NONE) return;
			else if (type == BlockType.ENEMY_SPAWN) {
				enemySpawnPos.add(new Vec2(x, y));
				return;
			} else if (type == BlockType.PLAYER_SPAWN) {
				if (playerSpawnPos[0] == null) playerSpawnPos[0] = new Vec2(x, y);
				else if (playerSpawnPos[1] == null) playerSpawnPos[1] = new Vec2(x, y);
				return;
			}
		}

		try {
			new Block(type, x, y, force);
		} catch (IllegalStateException ignored) {}
	}

	private static String @NotNull [] parseData(@NotNull String line) {
		return line.substring(2).split(" ");
	}
}
