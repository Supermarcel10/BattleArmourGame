package game.main;

import java.io.FileInputStream;

import game.IO.AM;
import game.IO.Config;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.Player;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * A class that handles all sound effects and background music.
 * <p>
 *     This class uses a {@link BlockingQueue} to queue up sound effects to be played.
 *     The queue is processed in a separate thread.
 *     <br>
 *     This class also uses a separate thread to play background music.
 *     The background music is played in a loop.
 * </p>
 */
public class SoundHandler implements Runnable {
	private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
	private Thread thread;

	private int currentSongIndex = 0;
	private Thread bgmThread;

	/**
	 * Creates a new sound handler.
	 */
	public SoundHandler() {
		playBackgroundMusic();
	}

	/**
	 * Plays the background music.
	 * <p>
	 *     This method plays the background music in a loop.
	 *     The background music is played in a separate thread.
	 * </p>
	 */
	public void playBackgroundMusic() {
		String[] musicArray = AM.music.values().toArray(new String[0]);

		if (bgmThread == null || !bgmThread.isAlive()) {
			bgmThread = new Thread(() -> {
				try {
					while (!Thread.currentThread().isInterrupted()) {
						CustomPlayer player = new CustomPlayer(new FileInputStream(musicArray[currentSongIndex]));
						player.setVolume(Config.musicVolume);
						player.play();

						currentSongIndex = (currentSongIndex + 1) % musicArray.length;
					}
				} catch (Exception ignored) {
				}
			});
			bgmThread.start();
		}
	}

	/**
	 * Stops the background music.
	 */
	public void stopBackgroundMusic() {
		if (bgmThread != null && bgmThread.isAlive()) {
			bgmThread.interrupt();
		}
	}

	/**
	 * Plays a sound effect from the queue.
	 * @param fileName The file directory of the sound effect.
	 */
	public void play(String fileName) {
		if (fileName == null) return;

		boolean added = queue.offer(fileName);
		if (!added) return;

		if (thread == null || !thread.isAlive()) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Plays a sound effect in a new thread.
	 */
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				CustomPlayer player = new CustomPlayer(new FileInputStream(queue.take()));
				player.setVolume(Config.soundVolume);

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


/**
 * A custom player that allows for volume control.
 * <p>
 *     This class is based on the {@link javazoom.jl.player.Player} class.
 *     The only difference is that it uses a custom audio device that allows for volume control.
 * </p>
 */
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


/**
 * A custom audio device that allows for volume control.
 * <p>
 *     This class is based on the {@link javazoom.jl.player.JavaSoundAudioDevice} class.
 *     The only difference is that it allows for volume control.
 * </p>
 */
class CustomJavaSoundAudioDevice extends JavaSoundAudioDevice {
	private float volume = 1.0f;

	/**
	 * Sets the volume of the audio device.
	 * @param volume The volume, between 0 and 1.
	 */
	public void setVolume(float volume) {
		this.volume = volume;
	}

	/**
	 * Gets the volume of the audio device.
	 * @return The volume, between 0 and 1.
	 */
	public float getVolume() {
		return volume;
	}

	/**
	 * Writes the audio samples to the audio device.
	 * @param samples The audio samples.
	 * @param offs The offset in the array.
	 * @param len The number of samples to write.
	 * @throws JavaLayerException If an error occurs.
	 */
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
