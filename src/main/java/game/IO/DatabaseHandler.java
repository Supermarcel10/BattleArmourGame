package game.IO;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HexFormat;

import static game.MainGame.*;
import static game.window.WindowHandler.view;


/**
 * Handles the database connection and high scores.
 */
public class DatabaseHandler {
    public static Connection connection;

    /**
     * Connects to the database.
     */
    private static void connectToDB() {
        if (connection != null) return;

        try {
            // Load the driver class
            Class.forName("org.sqlite.JDBC");

            // Establish a connection with the database
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/scores/scores.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects from the database.
     */
    public static void disconnectFromDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the high scores table if it does not exist.
     */
    private static void createTable() {
        try {
            Statement statement = connection.createStatement();

            statement.execute("""
                CREATE TABLE IF NOT EXISTS high_scores (
                    name VARCHAR(255),
                    level VARCHAR(255),
                    score INT,
                    killed INT,
                    blocks_broken INT
                );
                """);

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a high score to the database.
     * If the player already has a high score, the score will be updated if it is higher than the existing score.
     * @param name The name of the player.
     * @return true if the score was added, false if the score was not added.
     */
    public static boolean addHighScore(String name) {
        connectToDB();
        createTable();

        name = name.toUpperCase();

        try {
            String levelHash = getSHA256Hash(String.valueOf(currentLevel));

            PreparedStatement selectStatement = connection.prepareStatement("""
            SELECT score FROM high_scores WHERE name = ? AND level = ?
            """);

            selectStatement.setString(1, name);
            selectStatement.setString(2, levelHash);

            ResultSet resultSet = selectStatement.executeQuery();
            boolean rowExists = resultSet.next();

            int existingScore = 0;
            if (rowExists) {
                existingScore = resultSet.getInt("score");
            }

            resultSet.close();
            selectStatement.close();

            if (rowExists && score > existingScore) {
                // Score exists and is lower than current score
                PreparedStatement updateStatement = connection.prepareStatement("""
                UPDATE high_scores SET score = ?, killed = ?, blocks_broken = ? WHERE name = ? AND level = ?
                """);

                updateStatement.setInt(1, score);
                updateStatement.setInt(2, kills);
                updateStatement.setInt(3, brokenBlocks);
                updateStatement.setString(4, name);
                updateStatement.setString(5, levelHash);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else if (!rowExists) {
                // Score does not exist
                PreparedStatement insertStatement = connection.prepareStatement("""
                INSERT INTO high_scores (name, level, score, killed, blocks_broken) VALUES (?, ?, ?, ?, ?)
                """);

                insertStatement.setString(1, name);
                insertStatement.setString(2, levelHash);
                insertStatement.setInt(3, score);
                insertStatement.setInt(4, kills);
                insertStatement.setInt(5, brokenBlocks);
                insertStatement.executeUpdate();
                insertStatement.close();
            } else {
                // Score exists but is higher than current score
                JOptionPane.showMessageDialog(view,
                        "Current score is lower than high score!\nScore has not been saved.",
                        "Score not saved", JOptionPane.INFORMATION_MESSAGE);
            }

            return true;
        } catch (SQLException | NoSuchAlgorithmException | IOException ignored) {
            return false;
        }
    }

    /**
     * Gets the SHA-512 hash of a file.
     * @param fileName The name of the file.
     * @return The SHA-512 hash of the file.
     * @throws NoSuchAlgorithmException If the SHA-512 algorithm is not found.
     * @throws IOException If the file does not exist.
     */
    public static @NotNull String getSHA256Hash(String fileName) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        try (InputStream is = new FileInputStream(fileName)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] hash = digest.digest();
            return bytesToHex(hash);
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     * @param bytes The byte array to convert.
     * @return The hexadecimal string.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull String bytesToHex(byte @NotNull [] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
