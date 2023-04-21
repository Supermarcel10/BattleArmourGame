package game.window;

import game.IO.Config;
import game.main.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static game.Game.*;


public class WindowDeath extends WindowHandler {
	public static void createDeathMenu() {
		lblScore.setVisible(false);

		pnlOverlay.setBackground(new Color(0.9f, 0, 0, 0));
		pnlOverlay.setOpaque(true);

		final boolean[] complete = new boolean[]{false, false};

		new Timer(10, new ActionListener() {
			float alpha = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				alpha += 0.005f;
				if (alpha >= 0.3f) {
					alpha = 1f;
					complete[0] = true;
					((Timer) e.getSource()).stop();
				}
				pnlOverlay.setBackground(new Color(0.9f, 0, 0, alpha));
				pnlOverlay.repaint();
			}
		}).start();

		JLabel lblGameOver = createText("GAME OVER!", 1.25f, pnlOverlay, 0);

		int gameOverLabelWidth = (int) Config.resolution.x;
		int gameOverLabelHeight = (int) (Config.resolution.y / gridSize) * 2;
		int gameOverLabelX = (pnlMain.getPreferredSize().width - gameOverLabelWidth) / 2;

		int startGameOverLabelY = (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 1f));
		int endGameOverLabelY = (int) ((Config.resolution.y / gridSize) + (2f * scaleFactor));

		lblGameOver.setBounds(gameOverLabelX, startGameOverLabelY, gameOverLabelWidth, gameOverLabelHeight);

		Timer timer = new Timer(10, e -> {
			if (complete[0] && lblGameOver.getY() > endGameOverLabelY) {
				pnlOverlay.repaint();
				lblGameOver.setLocation(lblGameOver.getX(), (int) (lblGameOver.getY() - 4.5f * scaleFactor));
			} else if (complete[0]) {
				complete[1] = true;
				((Timer) e.getSource()).stop();
			}
		});

		timer.setInitialDelay(400);
		timer.start();

		JLabel lblScore = createText("Your score: " + score, 0.55f, pnlOverlay, 0);
		lblScore.setVisible(false);
		lblScore.setBounds(gameOverLabelX, (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 4f)), gameOverLabelWidth, gameOverLabelHeight);

		JLabel lblBlockBrokenScore = createText("Tanks destroyed: " + kills, 0.55f, pnlOverlay, 0);
		lblBlockBrokenScore.setVisible(false);
		lblBlockBrokenScore.setBounds(gameOverLabelX, (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 3f)), gameOverLabelWidth, gameOverLabelHeight);

		JLabel lblKillScore = createText("Blocks destroyed: " + brokenBlocks, 0.55f, pnlOverlay, 0);
		lblKillScore.setVisible(false);
		lblKillScore.setBounds(gameOverLabelX, (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) - 2f)), gameOverLabelWidth, gameOverLabelHeight);

		JButton btnMainMenu = createButton("Main Menu", .8f, pnlOverlay, 10);
		btnMainMenu.setVisible(false);
		JButton btnAddHighScore = createButton("Add High Score", .8f, pnlOverlay, 10);
		btnAddHighScore.setVisible(false);

		int btnMainMenuWidth = (int) Config.resolution.x / 2;
		int btnMainMenuHeight = (int) ((Config.resolution.y / gridSize) * 1.25f);
		int btnMainMenuX = (pnlMain.getPreferredSize().width - btnMainMenuWidth) / 2;
		int btnMainMenuY = (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) + 1f));

		int btnAddHighScoreWidth = (int) Config.resolution.x / 2;
		int btnAddHighScoreHeight = (int) ((Config.resolution.y / gridSize) * 1.25f);
		int btnAddHighScoreX = (pnlMain.getPreferredSize().width - btnAddHighScoreWidth) / 2;
		int btnAddHighScoreY = (int) ((Config.resolution.y / gridSize) * ((gridSize / 2f) + 3f));

		btnMainMenu.setBounds(btnMainMenuX, btnMainMenuY, btnMainMenuWidth, btnMainMenuHeight);
		btnAddHighScore.setBounds(btnAddHighScoreX, btnAddHighScoreY, btnAddHighScoreWidth, btnAddHighScoreHeight);

		Timer t2 = new Timer(10, e -> {
			if (complete[1]) {
				lblScore.setVisible(true);
				lblBlockBrokenScore.setVisible(true);
				lblKillScore.setVisible(true);
				btnMainMenu.setVisible(true);
				btnAddHighScore.setVisible(true);
				((Timer) e.getSource()).stop();
			}
		});

		t2.setInitialDelay(400);
		t2.start();

		btnMainMenu.addActionListener(e -> {
			gameState = GameState.MENU;
			resetGame();

			pnlOverlay.setVisible(false);
		});

		btnAddHighScore.addActionListener(e -> {

		});
	}
}
