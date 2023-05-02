package game.IO;

import java.sql.*;

import static game.MainGame.*;


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
                    name VARCHAR(255) PRIMARY KEY,
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

    public static void addHighScore(String name) {
        connectToDB();
        createTable();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("""
            INSERT INTO high_scores (name, score, killed, blocks_broken) VALUES (?, ?, ?, ?);
            """);

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, score);
            preparedStatement.setInt(3, kills);
            preparedStatement.setInt(4, brokenBlocks);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("""
                UPDATE high_scores SET score = ?, killed= ?, blocks_broken = ? WHERE name = ?;
                """);

                preparedStatement.setInt(1, score);
                preparedStatement.setInt(2, kills);
                preparedStatement.setInt(3, brokenBlocks);
                preparedStatement.setString(4, name);

                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
