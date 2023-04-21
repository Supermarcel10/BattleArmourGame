package game.main;

import game.objects.Block;
import game.objects.Pickup;
import game.prefab.BlockType;
import game.prefab.PickupType;
import game.prefab.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static game.main.Game.*;


public class LoadLevel {
	public static boolean loadLevel(File file) throws IOException {
		if (file == null) return false;

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		while ((line = br.readLine()) != null) {
			System.out.println(line);
			switch (line.charAt(0)) {
				case 'i' -> handleInfoParsing(line);
				case 'a' -> handleAudioParsing(line);
				case 'b' -> handleBlockPlacement(line);
				case 'p' -> handlePlayerPlacement(line);
				case 'o' -> handleObjectPlacement(line);
				case 'e' -> handleEnemyPlacement(line);
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

	public static boolean loadLevel(String path) throws IOException {
		return loadLevel(new File(path));
	}

	private static void handleInfoParsing(@NotNull String line) {
		// TODO: IMPLEMENT
		System.out.println("Level information: " + line.substring(2));
	}

	private static void handleAudioParsing(@NotNull String line) {
		// TODO: IMPLEMENT
		System.out.println("Audio information: " + line.substring(2));
	}

	private static void handleBlockPlacement(String line) {
		String[] parsedData = parseData(line);
		int x = Integer.parseInt(parsedData[1]);
		int y = Integer.parseInt(parsedData[2]);

		try {
			new Block(BlockType.valueOf(parsedData[0]), x, y);
		} catch (IllegalStateException ignored) {}
	}

	private static void handlePlayerPlacement(String line) {
		String[] parsedData = parseData(line);
		int x = Integer.parseInt(parsedData[1]);
		int y = Integer.parseInt(parsedData[2]);
		player[getPlayerNumber(parsedData[0])] = new Player(x, y);
	}

	private static void handleObjectPlacement(String line) {
		String[] parsedData = parseData(line);
		int x = Integer.parseInt(parsedData[1]);
		int y = Integer.parseInt(parsedData[2]);
		switch (parsedData[0].split(":")[0]) {
			case "PICKUP" -> new Pickup(PickupType.valueOf(parsedData[0].split(":")[1]), x, y);
		}
	}

	private static void handleEnemyPlacement(@NotNull String line) {
		// TODO: IMPLEMENT
		System.out.println("Enemy information: " + line.substring(2));
	}

	private static String @NotNull [] parseData(@NotNull String line) {
		return line.substring(2).split(" ");
	}

	private static int getPlayerNumber(@NotNull String playerData) {
		return Integer.parseInt(String.valueOf(playerData.charAt(6))) - 1;
	}
}
