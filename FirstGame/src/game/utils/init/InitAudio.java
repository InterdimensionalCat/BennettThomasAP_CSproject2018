package game.utils.init;

import java.util.HashMap;

import game.Game;
import game.audio.AudioFile;

public class InitAudio implements Runnable {

	public static volatile HashMap<String, AudioFile> musicFiles = new HashMap<String, AudioFile>();
	public static volatile String[] audioFileNames = {"PlayerDash1" , "PlayerDash2" , "PlayerDash3" , "PlayerDash4", "PlayerDead", "PlayerFall1", "PlayerJump1", "PlayerJump2", "BoopDeath", "BoopDeath2", "PlayerWin" };

	@Override
	public void run() {
		System.out.println("Loading Audio");
		
		for(String s : audioFileNames) {
			musicFiles.put(s, new AudioFile("src/assets/audio/" + s + ".wav"));
		}
		
		System.out.println("Audio Loaded");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Game.driver.getScreen().setProgress((++Game.taskComplete) * 25);
	}
	
}
