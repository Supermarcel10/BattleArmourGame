package game.window;


import game.IO.Config;

import javax.swing.*;

import java.io.File;

import static game.MainGame.*;
import static game.MainGame.score;
import static game.window.WindowHandler.pnlMain;
import static game.window.WindowHandler.pnlOverlay;

public class WindowPlay extends WindowCommons {
    protected static JLabel lblScore;

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

    public static void selectLevel() {
        File file = WindowCommons.selectLoadFile();

        if (file == null) {
            // Return to main menu
            WindowMenu.showMenu();
        } else {
            // TODO: FINISH OFF
        }
    }

    /**
     * Updates the score label.
     */
    public static void updateScore() {
        if (lblScore != null) lblScore.setText("SCORE: " + score);
    }
}
