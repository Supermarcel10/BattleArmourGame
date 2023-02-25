package game;

import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Listener implements KeyListener {
	@Override
	public void keyTyped(KeyEvent e) {}


	@Override
	public void keyPressed(@NotNull KeyEvent e) {
		System.out.println("Key pressed: " + e.getKeyChar());
	}


	@Override
	public void keyReleased(@NotNull KeyEvent e) {
		System.out.println("Key released: " + e.getKeyChar());
	}
}
