package game.main;

import city.cs.engine.SoundClip;
import game.input.Config;
import java.io.FileInputStream;
import javazoom.jl.player.Player;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SoundHandler implements Runnable {
	private final BlockingQueue<String> queue;
	private Thread thread;

	public static void playBackgroundMusic() {
		try {
			SoundClip backgroundMusic = new SoundClip(Config.music.get("game"));
			backgroundMusic.loop();
			backgroundMusic.play();
		} catch (Exception ignored) {}
	}

	public SoundHandler() {
		queue = new LinkedBlockingQueue<>();
	}

	public void play(String fileName) {
		queue.offer(fileName);

		if (thread == null || !thread.isAlive()) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		queue.clear();
	}

	public void run() {
		try {
			while (true) {
				String fileName = queue.take();

				FileInputStream fis = new FileInputStream(fileName);
				Player player = new Player(fis);
				player.play();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
