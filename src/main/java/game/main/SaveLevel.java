package game.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static game.main.Game.*;

public class SaveLevel {
	public static void saveLevel(String fileLocation) throws IOException {
		File file = new File(fileLocation);

//		// Create the file if it doesn't exist.
//		if (!file.exists()) //noinspection ResultOfMethodCallIgnored
//			file.createNewFile();

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

		// Save all blocks
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (blocks[i][j] != null) {
					bufferedWriter.write("b " + blocks[i][j].type + " " + (i - hGridSize) + " " + (j - hGridSize) + "\n");
					System.out.println("b " + blocks[i][j].type + " " + (i - hGridSize) + " " + (j - hGridSize));
				}
			}
		}

		// Close the BufferedWriter
		bufferedWriter.write("END");
		bufferedWriter.close();
	}
}
