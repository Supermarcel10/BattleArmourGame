package game.input;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import game.main.WindowHandler;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.event.*;

import static game.main.Game.*;
import static game.main.WindowHandler.view;


public class Listener implements KeyListener, MouseListener, StepListener {
	@Override public void mouseEntered(MouseEvent e) {
		view.requestFocus();
	}

	@Override
	public void keyPressed(@NotNull KeyEvent e) {
		if (player == null) return;

		switch (e.getKeyCode()) {
			case KeyEvent.VK_A -> player.setMoveDirection(new Vec2(-1, 0));
			case KeyEvent.VK_D -> player.setMoveDirection(new Vec2(1, 0));
			case KeyEvent.VK_W -> player.setMoveDirection(new Vec2(0, 1));
			case KeyEvent.VK_S -> player.setMoveDirection(new Vec2(0, -1));
			default -> {}
		}
	}

	@Override
	public void keyReleased(@NotNull KeyEvent e) {
		if (player == null) return;

		switch (e.getKeyCode()) {
			case KeyEvent.VK_A, KeyEvent.VK_D -> player.setMoveDirection(new Vec2(0, player.getMoveDirection().y));
			case KeyEvent.VK_W, KeyEvent.VK_S -> player.setMoveDirection(new Vec2(player.getMoveDirection().x, 0));
			case KeyEvent.VK_SPACE -> player.shoot();
			default -> {}
		}
	}

	@Override
	public void preStep(StepEvent stepEvent) {
		if (player != null) player.update();

		if (blocks[(int) basePos.x][(int) basePos.y].health <= 0 || (player != null && player.health <= 0)) {
			WindowHandler.createDeathMenu();
			world.stop();
		}

		if (postUpdateScore != score) {
			WindowHandler.updateScore();
			postUpdateScore = score;
		}
	}

	@Override public void postStep(StepEvent stepEvent) {}
	@Override public void mouseClicked(@NotNull MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}
}
