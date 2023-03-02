package main.java.game;

import city.cs.engine.CircleShape;
import city.cs.engine.DynamicBody;
import javax.swing.Timer;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.event.*;

import static main.java.game.WindowHandler.view;


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


	/**
	 * Invoked when the mouse button has been clicked (pressed
	 * and released) on a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseClicked(@NotNull MouseEvent e) {
		System.out.printf("Mouse button %d clicked at %d, %d%n", e.getButton(), e.getX(), e.getY());

		DynamicBody ball = new DynamicBody(view.getWorld(), new CircleShape(1f));
		ball.setPosition(view.viewToWorld(e.getPoint()));
	}


	/**
	 * Invoked when a mouse button has been pressed on a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mousePressed(MouseEvent e) {

	}


	/**
	 * Invoked when a mouse button has been released on a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}


	/**
	 * Invoked when the mouse enters a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("Mouse entered");
		view.requestFocus();
	}


	/**
	 * Invoked when the mouse exits a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}


	/**
	 * Invoked when a key has been typed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key typed event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}


	/**
	 * Invoked when a key has been pressed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key pressed event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyPressed(@NotNull KeyEvent e) {
		System.out.println("Key pressed: " + e.getKeyChar());

		switch (e.getKeyCode()) {
			case KeyEvent.VK_A -> player.setMoveDir(new Vec2(-1, 0));
			case KeyEvent.VK_D -> player.setMoveDir(new Vec2(1, 0));
			case KeyEvent.VK_W -> player.setMoveDir(new Vec2(0, 1));
			case KeyEvent.VK_S -> player.setMoveDir(new Vec2(0, -1));
			default -> {}
		}
	}


	/**
	 * Invoked when a key has been released.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key released event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyReleased(@NotNull KeyEvent e) {
		System.out.println("Key released: " + e.getKeyChar());

		switch (e.getKeyCode()) {
			case KeyEvent.VK_A, KeyEvent.VK_D -> player.setMoveDir(new Vec2(0, player.moveDir.y));
			case KeyEvent.VK_W, KeyEvent.VK_S -> player.setMoveDir(new Vec2(player.moveDir.x, 0));
			default -> {}
		}
	}
}
