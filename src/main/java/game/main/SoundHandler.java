package game.main;

import java.io.FileInputStream;

import game.IO.AM;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.Player;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SoundHandler implements Runnable {
	private final static float soundVolume= 0f, musicVolume = 0f;

	private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
	private Thread thread;

	private int currentSongIndex = 0;
	private Thread bgmThread;

	public SoundHandler() {
		playBackgroundMusic();
	}

	public void playBackgroundMusic() {
		String[] musicArray = AM.music.values().toArray(new String[0]);

		if (bgmThread == null || !bgmThread.isAlive()) {
			bgmThread = new Thread(() -> {
				try {
					while (!Thread.currentThread().isInterrupted()) {
						CustomPlayer player = new CustomPlayer(new FileInputStream(musicArray[currentSongIndex]));
						player.setVolume(musicVolume);
						player.play();

						currentSongIndex = (currentSongIndex + 1) % musicArray.length;
					}
				} catch (Exception ignored) {
				}
			});
			bgmThread.start();
		}
	}

	public void stopBackgroundMusic() {
		if (bgmThread != null && bgmThread.isAlive()) {
			bgmThread.interrupt();
		}
	}

	public void play(String fileName) {
		if (fileName == null) return;

		boolean added = queue.offer(fileName);
		if (!added) return;

		if (thread == null || !thread.isAlive()) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				CustomPlayer player = new CustomPlayer(new FileInputStream(queue.take()));
				player.setVolume(soundVolume);

				new Thread(() -> {
					try {
						player.play();
					} catch (JavaLayerException ignored) {}
				}).start();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception ignored) {}
	}
}


class CustomPlayer extends Player {
	private final CustomJavaSoundAudioDevice audioDevice;

	public CustomPlayer(InputStream stream) throws JavaLayerException {
		this(stream, new CustomJavaSoundAudioDevice());
	}

	public CustomPlayer(InputStream stream, CustomJavaSoundAudioDevice device) throws JavaLayerException {
		super(stream, device);
		audioDevice = device;
	}

	public void setVolume(float gain) {
		audioDevice.setVolume(gain);
	}

	public float getVolume() {
		return audioDevice.getVolume();
	}
}


class CustomJavaSoundAudioDevice extends JavaSoundAudioDevice {
	private float volume = 1.0f;

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getVolume() {
		return volume;
	}

	@Override
	protected void writeImpl(short[] samples, int offs, int len) throws JavaLayerException {
		if (volume != 1.0f) {
			for (int i = offs; i < offs + len; i++) {
				samples[i] = (short) Math.min(Math.max((int) (samples[i] * volume), Short.MIN_VALUE), Short.MAX_VALUE);
			}
		}
		super.writeImpl(samples, offs, len);
	}
}
