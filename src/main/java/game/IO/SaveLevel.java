package game.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static game.MainGame.*;


/**
 * This class is used to save a level to a file.
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
public class SaveLevel {
	/**
	 * Save the level to a file in the format.
	 * @param file The file to save to.
	 * @throws IOException
	 */
	public static void saveLevel(File file) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

		// Save all blocks
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (blocks[i][j] != null) {
					bufferedWriter.write("b " + blocks[i][j].type + " " + (i - hGridSize) + " " + (j - hGridSize) + "\n");
				}
			}
		}

		// Close the BufferedWriter
		bufferedWriter.write("END");
		bufferedWriter.close();
	}
}
