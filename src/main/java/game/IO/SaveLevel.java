package game.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static game.MainGame.*;

public class SaveLevel {
	public static void saveLevel(File file) throws IOException {
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
