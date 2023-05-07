package game.main;

import java.io.FileInputStream;

import game.IO.AM;
import game.IO.Config;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


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

	private final CustomPlayer[] playerPlayer = new CustomPlayer[2];
	private final Thread[] playerThread = new Thread[2];

	private CustomPlayer menuPlayer;
	private Thread menuThread;

	private static final String[] musicArray = AM.music.values().toArray(new String[0]);
	private static final AtomicInteger currentSongIndex = new AtomicInteger(new Random().nextInt(musicArray.length));
	private CustomPlayer bgmPlayer;
	private Thread bgmThread;

	/**
	 * Creates a new sound handler.
	 */
	public SoundHandler() {}

	/**
	 * Plays the background music.
	 * <p>
	 *     This method plays the background music in a loop.
	 *     The background music is played in a separate thread.
	 * </p>
	 */
	public void playBackgroundMusic() {
		if (bgmThread != null) bgmPlayer = null;

		bgmThread = new Thread(() -> {
			try {
				while (bgmPlayer == null || !bgmPlayer.stopRequested) {
					bgmPlayer = new CustomPlayer(new FileInputStream(musicArray[currentSongIndex.get()]));
					bgmPlayer.setVolume(Config.musicVolume);
					bgmPlayer.play();
					currentSongIndex.set((currentSongIndex.get() + 1) % musicArray.length);
				}
			} catch (JavaLayerException | FileNotFoundException ignored) {}
		});
		bgmThread.start();
	}

	/**
	 * Stops the background music.
	 */
	public void stopBackgroundMusic() {
		if (bgmThread != null) {
			bgmThread.interrupt();
			bgmPlayer.stop();
		}
	}

	public void playMenuMusic() {
		if (menuThread != null) menuPlayer = null;

		menuThread = new Thread(() -> {
			try {
				while (menuPlayer == null || !menuPlayer.stopRequested) {
					menuPlayer = new CustomPlayer(new FileInputStream(AM.miscSound.get("menuMusic")));
					menuPlayer.setVolume(Config.musicVolume);
					menuPlayer.play();
				}
			} catch (JavaLayerException | FileNotFoundException ignored) {}
		});
		menuThread.start();
	}

	public void stopMenuMusic() {
		if (menuThread != null) {
			menuThread.interrupt();
			menuPlayer.stop();
		}
	}

	public void playPlayerMovement(int playerIndex) {
		if (playerPlayer[playerIndex] != null && !playerPlayer[playerIndex].stopRequested) return;

		if (playerThread[playerIndex] != null) playerPlayer[playerIndex] = null;

		playerThread[playerIndex] = new Thread(() -> {
			try {
				while (playerPlayer[playerIndex] == null || !playerPlayer[playerIndex].stopRequested) {
					playerPlayer[playerIndex] = new CustomPlayer(new FileInputStream(AM.tankSound.get("tankMove")));
					playerPlayer[playerIndex].setVolume(Config.musicVolume);
					playerPlayer[playerIndex].play();
				}
			} catch (JavaLayerException | FileNotFoundException ignored) {}
		});
		playerThread[playerIndex].start();
	}

	public void stopPlayerMovement(int playerIndex) {
		if (playerThread[playerIndex] != null) {
			playerThread[playerIndex].interrupt();
			playerPlayer[playerIndex].stop();
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

				new Thread(player::play).start();
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
class CustomPlayer extends AdvancedPlayer {
	private final CustomJavaSoundAudioDevice audioDevice;
	public boolean stopRequested = false;

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

	@Override
	public void play() {
		stopRequested = false;

		try {
			super.play();
		} catch (JavaLayerException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public boolean play(int frames) {
		stopRequested = false;

		try {
			return super.play(frames);
		} catch (JavaLayerException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void stop() {
		stopRequested = true;
		close();
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
