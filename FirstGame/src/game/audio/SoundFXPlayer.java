package game.audio;

import game.utils.ThreadPool;
import game.utils.init.InitAudio;

public class SoundFXPlayer implements Runnable {
	private String currentSong;
	protected boolean running;
	private ThreadPool pool;
	private int globalFXVolume = -20;
	
	public SoundFXPlayer(String... files) {
		if(Runtime.getRuntime().availableProcessors() > 4) {
			pool = new ThreadPool(2);
		} else {
			pool = new ThreadPool(1);
		}
	}

	@Override
	public void run() {
		running = true;
		AudioFile song = InitAudio.musicFiles.get(currentSong);
		song.play(globalFXVolume);
		while (running) {
			if(!song.isPlaying()) {
				running = false;
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void playSound(String fx) {
		this.currentSong = fx;
		pool.runTask(this);
	}
	
	public void playSound(String fx, int volume) {
		globalFXVolume = volume;
		playSound(fx);
	}
}
