package game.main;

import city.cs.engine.SoundClip;
import game.input.Config;


public class SoundHandler {
	public static void playBackgroundMusic() {
		try {
			SoundClip backgroundMusic = new SoundClip(Config.music.get("game"));
			backgroundMusic.loop();
			backgroundMusic.play();
		} catch (Exception __) {}
	}
}
