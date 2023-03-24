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
		if (player[0] != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A -> player[0].setMoveDirection(new Vec2(-1, 0));
				case KeyEvent.VK_D -> player[0].setMoveDirection(new Vec2(1, 0));
				case KeyEvent.VK_W -> player[0].setMoveDirection(new Vec2(0, 1));
				case KeyEvent.VK_S -> player[0].setMoveDirection(new Vec2(0, -1));
				case KeyEvent.VK_SPACE -> player[0].shoot();
			}
		} else if (player[1] != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT -> player[1].setMoveDirection(new Vec2(-1, 0));
				case KeyEvent.VK_RIGHT -> player[1].setMoveDirection(new Vec2(1, 0));
				case KeyEvent.VK_UP -> player[1].setMoveDirection(new Vec2(0, 1));
				case KeyEvent.VK_DOWN -> player[1].setMoveDirection(new Vec2(0, -1));
				case KeyEvent.VK_ENTER -> player[1].shoot();
			}
		}
	}

	@Override
	public void keyReleased(@NotNull KeyEvent e) {
		if (player[0] != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A, KeyEvent.VK_D -> player[0].setMoveDirection(new Vec2(0, player[0].getMoveDirection().y));
				case KeyEvent.VK_W, KeyEvent.VK_S -> player[0].setMoveDirection(new Vec2(player[0].getMoveDirection().x, 0));
			}
		} else if (player[1] != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> player[1].setMoveDirection(new Vec2(0, player[1].getMoveDirection().y));
				case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> player[1].setMoveDirection(new Vec2(player[0].getMoveDirection().x, 0));
			}
		}
	}

	@Override
	public void preStep(StepEvent stepEvent) {
		if (player[0] != null) player[0].update();
		if (player[1] != null) player[1].update();

		if (blocks[(int) basePos.x][(int) basePos.y].health <= 0 || ((player[0] != null && player[0].health <= 0) && (player[1] != null && player[1].health <= 0))) {
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
