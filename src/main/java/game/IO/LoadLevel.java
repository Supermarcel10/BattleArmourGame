package game.IO;

import game.objects.block.Block;
import game.objects.block.BlockType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static game.MainGame.*;


/**
 * This class is used to load a level from a file.
 * <p>
 *     The ideology of the level file is as follows:
 *     <ul>
 *         <li>Each line is a command.</li>
 *         <li>Each command is case sensitive.</li>
 *         <li>Each command is executed in the order it is written.</li>
 *         <li>Each command is a single character followed by a space and then the data.</li>
 *     </ul>
 * </p>
 * <p>
 *     The following commands are available:
 *     <ul>
 *         <li><b>i</b> - Information about the level.</li>
 *         <li><b>a</b> - Custom audio tracks for the level. TO BE IMPLEMENTED</li>
 *         <li><b>b</b> - Information about a block in a position.</li>
 *         <li><b>END</b> - End of level. This is used to indicate the end of the level.</li>
 *     </ul>
 * </p>
 *
 * <p>
 *     The game level creator will automatically generate a level file for you in the correct format.
 *     e.g.
 *     <ul>
 *     <li><code>i GRID15</code></li>
 *     <li><code>b BRICK 1 0</code></li>
 *     </ul>
 * </p>
 */
public class LoadLevel {
	/**
	 * Load a level from a file.
	 *
	 * @param file The file to load the level from.
	 * @param force If true, the block will be placed regardless of the block type.
	 * @return True if the level was loaded successfully, false otherwise.
	 * @throws IOException
	 */
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

		// Always make the map border after loading the level.
		makeMapBorder();

		// If END is not found, return failure to load.
		br.close();
		return false;
	}

	/**
	 * Load a level from a file without forcing the block placement.
	 *
	 * @param file The file to load the level from.
	 * @return True if the level was loaded successfully, false otherwise.
	 * @throws IOException
	 */
	public static boolean loadLevel(File file) throws IOException {
		return loadLevel(file, false);
	}

	/**
	 * Load a level from a file path.
	 *
	 * @param path The path to the file to load the level from.
	 * @param force If true, the block will be placed regardless of the block type.
	 * @return True if the level was loaded successfully, false otherwise.
	 * @throws IOException
	 */
	public static boolean loadLevel(String path, boolean force) throws IOException {
		return loadLevel(new File(path), force);
	}


	/**
	 * Load a level from a file path without forcing the block placement.
	 *
	 * @param path The path to the file to load the level from.
	 * @return True if the level was loaded successfully, false otherwise.
	 * @throws IOException
	 */
	public static boolean loadLevel(String path) throws IOException {
		return loadLevel(new File(path), false);
	}

	/**
	 * Handling of custom level information.
	 * TO BE IMPLEMENTED!
	 * @param line The line of the level file.
	 */
	private static void handleInfoParsing(@NotNull String line) {
		// TODO: Implement custom level information
		System.out.println("Level information: " + line.substring(2));
	}

	/**
	 * Handling of custom audio information.
	 * TO BE IMPLEMENTED!
	 * @param line The line of the level file.
	 */
	private static void handleAudioParsing(@NotNull String line) {
		// TODO: Implement custom audio information
		System.out.println("Audio information: " + line.substring(2));
	}

	/**
	 * Handling of block placement.
	 *
	 * @param line The line of the level file.
	 * @param force If true, the block will be placed regardless of the block type.
	 */
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

	/**
	 * Parse the data from a line.
	 *
	 * @param line The line of the level file.
	 * @return The parsed data.
	 */
	private static String @NotNull [] parseData(@NotNull String line) {
		return line.substring(2).split(" ");
	}
}
