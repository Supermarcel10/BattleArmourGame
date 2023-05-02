package game.IO;

import javax.swing.*;
import java.sql.*;

import static game.MainGame.*;
import static game.window.WindowHandler.view;


public class DatabaseHandler {
    public static Connection connection;

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

    public static void disconnectFromDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static boolean addHighScore(String name) {
        connectToDB();
        createTable();

        name = name.toUpperCase();

        try {
            PreparedStatement selectStatement = connection.prepareStatement("""
            SELECT score FROM high_scores WHERE name = ? AND level = ?
            """);

            selectStatement.setString(1, name);
            selectStatement.setString(2, currentLevel.getName());

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
                updateStatement.setString(5, currentLevel.getName());
                updateStatement.executeUpdate();
                updateStatement.close();
            } else if (!rowExists) {
                // Score does not exist
                PreparedStatement insertStatement = connection.prepareStatement("""
                INSERT INTO high_scores (name, level, score, killed, blocks_broken) VALUES (?, ?, ?, ?, ?)
                """);

                insertStatement.setString(1, name);
                insertStatement.setString(2, currentLevel.getName());
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
        } catch (SQLException e) {
            return false;
        }
    }
}
