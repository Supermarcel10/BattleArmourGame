package game;

import city.cs.engine.CircleShape;
import city.cs.engine.DynamicBody;
import javax.swing.Timer;

import game.character.Player;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.event.*;

import static game.WindowHandler.view;


public class Listener implements KeyListener, MouseListener {
	private final Player player;
	private long lastTime = System.nanoTime();

	public Listener(Player player) {
		this.player = player;
		Timer timer = new Timer(1, e -> {
			long currentTime = System.nanoTime();
			long elapsedTime = currentTime - lastTime;
			lastTime = currentTime;
			player.update(elapsedTime);
		});
		timer.start();
	}

	@Override
	public void mouseClicked(@NotNull MouseEvent e) {
		System.out.printf("Mouse button %d clicked at %d, %d%n", e.getButton(), e.getX(), e.getY());

		DynamicBody ball = new DynamicBody(view.getWorld(), new CircleShape(1f));
		ball.setPosition(view.viewToWorld(e.getPoint()));
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		view.requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(@NotNull KeyEvent e) {
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
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A, KeyEvent.VK_D -> player.setMoveDirection(new Vec2(0, player.getMoveDirection().y));
			case KeyEvent.VK_W, KeyEvent.VK_S -> player.setMoveDirection(new Vec2(player.getMoveDirection().x, 0));
			case KeyEvent.VK_SPACE -> player.shoot();
			default -> {}
		}
	}
}
