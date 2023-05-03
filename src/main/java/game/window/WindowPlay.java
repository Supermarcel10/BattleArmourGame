package game.window;

import game.IO.Config;
import game.MainGame;
import game.main.GameState;
import game.window.customAssets.CustomButtonUI;
import game.window.customAssets.CustomLayeredPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static game.MainGame.*;
import static game.MainGame.score;
import static game.window.WindowHandler.*;


public class WindowPlay extends WindowCommons {
    protected static JLabel lblScore;
    protected static CustomLayeredPane pnlPlayerSelection = new CustomLayeredPane(new Color(162, 227, 232));
    protected static HashMap<JButton, Runnable> btnsMenu = new LinkedHashMap<>() {{
        put(createButton("SINGLEPLAYER", 1f, pnlPlayerSelection, 100), () -> startGame(1));
        put(createButton("MULTIPLAYER", 1f, pnlPlayerSelection, 100), () -> startGame(2));
        put(createButton("BACK", 1f, pnlPlayerSelection, 100), WindowMenu::showMenu);
    }};

    /**
     * Creates the game overlay.
     */
    public static void createGameOverlay() {
        if (lblScore == null) {
            lblScore = createText("SCORE: " + score, 1, pnlOverlay, 1);
            int lblScoreX = (int) ((pnlMain.getPreferredSize().width - Config.resolution.x) / 2);
            int lblScoreY = (int) (2 * scaleFactor);
            lblScore.setBounds(lblScoreX, lblScoreY, (int) Config.resolution.x, (int) Config.resolution.y / gridSize);
        } else {
            // Make the scoreLabel visible again.
            lblScore.setVisible(true);
        }
    }

    public static @Nullable File selectLevel() {
        File file = WindowCommons.selectLoadFile();

        if (file == null) {
            // Return to main menu
            WindowMenu.showMenu();
            return null;
        }
        else return file;
    }

    public static void selectNumOfPlayers() {
        if (setVisible(pnlPlayerSelection)) return;

        int btnWidth = (int) Config.resolution.x / 2;
        int btnHeight = (int) ((Config.resolution.y / gridSize) * 1.5f);
        int btnX = (pnlMain.getPreferredSize().width - btnWidth) / 2;
        int btnY = (int) (btnHeight * 1.5);

        for (JButton button : btnsMenu.keySet()) {
            button.setUI(new CustomButtonUI(new Color(45, 143, 150)));
            button.setBounds(btnX, btnY += btnHeight * 1.5, btnWidth, btnHeight);

            // Add action listener to each button
            button.addActionListener(e -> {
                btnsMenu.get(button).run();
            });
        }

        pnlMain.add(pnlPlayerSelection, CustomLayeredPane.DEFAULT_LAYER);
        pnlPlayerSelection.setBounds(0, 0, view.getPreferredSize().width, view.getPreferredSize().height);
    }

    private static void startGame(int players) {
        numOfPlayers = players;

        File level = selectLevel();
        if (level == null) {
            return;
        }
        MainGame.loadGame(level);

        pnlPlayerSelection.setVisible(false);
        gameState = GameState.GAME;
    }

    public static void hideGameOverlay() {
        lblScore.setVisible(false);
    }

    /**
     * Updates the score label.
     */
    public static void updateScore() {
        if (lblScore != null) lblScore.setText("SCORE: " + score);
    }
}
