package game.audio;

import game.utils.ThreadPool;
import game.utils.init.InitAudio;

public class SoundFXPlayer implements Runnable {
	private String currentSong;
	protected boolean running;
	private ThreadPool pool;
	
	public SoundFXPlayer(String... files) {
		pool = new ThreadPool(2);
	}

	@Override
	public void run() {
		running = true;
		AudioFile song = InitAudio.musicFiles.get(currentSong);
		song.play();
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
}