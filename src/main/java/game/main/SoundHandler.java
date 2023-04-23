package game.main;

import game.IO.Config;
import java.io.FileInputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SoundHandler implements Runnable {
	private final BlockingQueue<String> queue;
	private Thread thread;

	public SoundHandler() {
		queue = new LinkedBlockingQueue<>();
	}

	public void playBackgroundMusic() {
		// Get a random background music and play it.
		int random = (int) (Math.random() * Config.music.size());
		// TODO: Make songs rotate.
		play(Config.music.get(Config.music.keySet().toArray()[random]));
	}

	public void play(String fileName) {
		if (fileName == null) {
			return;
		}

		boolean added = queue.offer(fileName);

		if (!added) {
			System.err.println("Failed to add " + fileName + " to sound queue!");
		}

		if (thread == null || !thread.isAlive()) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				String fileName = queue.take();

				FileInputStream fis = new FileInputStream(fileName);
				Player player = new Player(fis);
				new Thread(() -> {
					try {
						player.play();
					} catch (JavaLayerException ignored) {}
				}).start();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
