package game.window;

import game.IO.Config;
import game.IO.DatabaseHandler;
import game.window.customAssets.CustomButtonUI;
import game.window.customAssets.CustomLayeredPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static game.MainGame.gridSize;
import static game.window.WindowHandler.pnlMain;
import static game.window.WindowHandler.view;


public class WindowHighScore extends WindowCommons {
    protected static CustomLayeredPane pnlHighScore = new CustomLayeredPane(new Color(50, 9, 92));
    private static final CustomLayeredPane innerPane = new CustomLayeredPane(new Color(200, 186, 214));
    protected static HashMap<JButton, Runnable> btnsMenu = new LinkedHashMap<>() {{
        put(createButton("CHOOSE LEVEL", .8f, pnlHighScore, 0), WindowHighScore::chooseLevel);
        put(createButton("BACK", .8f, pnlHighScore, 0), WindowHighScore::hideHighScoreMenu);
    }};

    public static void createHighScoreOverlay() {
        if (setVisible(pnlHighScore)) {
            chooseLevel();
            return;
        }

        // Button size and position
        int btnWidth = (int) Config.resolution.x / 2;
        int btnHeight = (int) ((Config.resolution.y / gridSize) * 1.5f);
        int btnX = (pnlMain.getPreferredSize().width - btnWidth) / 2;
        int pnlMainHeight = pnlMain.getPreferredSize().height;

        // Make the inner panel transparent.
//        innerPane.setForeground(new Color(0, 0, 0, 255));
//        innerPane.setOpaque(false);

        // Add the inner panel to the high score panel.
        pnlHighScore.add(innerPane, JLayeredPane.DEFAULT_LAYER, 1);
        innerPane.setBounds(50, btnHeight / 2, pnlMain.getPreferredSize().width - 100, (int) (Config.resolution.y - (2.75 * btnHeight)));

        // Bottom button panel
        int btnY = pnlMainHeight - (3 * btnHeight); // Set initial Y position from bottom of screen.
        Color pressedColor = new Color(87, 0, 173); // Button pressed color.

        for (JButton button : btnsMenu.keySet()) {
            button.setForeground(new Color(255, 255, 255));
            button.setUI(new CustomButtonUI(pressedColor));
            button.setBounds(btnX, btnY += btnHeight, btnWidth, btnHeight);
            button.addActionListener(e -> btnsMenu.get(button).run());
        }

        pnlMain.add(pnlHighScore, CustomLayeredPane.DEFAULT_LAYER);
        pnlHighScore.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);

        chooseLevel();
    }

    private static void chooseLevel() {
        File selectedLevel = WindowCommons.selectLoadFile();

        if (selectedLevel != null) {
            // Clear the inner panel.
            innerPane.removeAll();
            innerPane.repaint();

            // Get the high scores from the database.
            ResultSet rs = DatabaseHandler.getHighScores(selectedLevel);

            int lblWidth = pnlMain.getPreferredSize().width / 2;
            int lblHeight = (int) ((Config.resolution.y / gridSize) * 1.1f);

            int i = 0;
            // Add each high score to the inner panel.
            try {
                while (rs != null && rs.next()) {
                    // Create a new label with the name and score.
                    JLabel lblName = createText(rs.getString("name"), .8f, innerPane, 2);
                    JLabel lblScore = createText(String.valueOf(rs.getInt("score")), .8f, innerPane, 2);

                    lblName.setForeground(Color.DARK_GRAY);
                    lblScore.setForeground(Color.GRAY);

                    lblName.setBounds(0, lblHeight * i, lblWidth, lblHeight);
                    lblScore.setBounds(lblWidth, lblHeight * i, lblWidth, lblHeight);

                    i++;
                }
            } catch (NullPointerException | SQLException e) {
                System.out.println("Error: Could not get high scores.");
                e.printStackTrace();
            }

//            innerPane.setBounds(0, 0, pnlMain.getPreferredSize().width, (int) (lblHeight * 1.1) * i);
        }
    }

    private static void hideHighScoreMenu() {
        pnlHighScore.setVisible(false);
        innerPane.removeAll();
        innerPane.repaint();
        WindowMenu.showMenu();
    }
}
