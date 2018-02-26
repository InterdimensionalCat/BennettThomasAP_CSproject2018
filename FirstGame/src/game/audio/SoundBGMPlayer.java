package game.audio;

import java.util.ArrayList;

public class SoundBGMPlayer implements Runnable {
	
	private ArrayList<AudioFile> musicFiles;
	private int currentSongIndex = 0;
	private boolean running;
	private AudioFile currentSong;
	private boolean active;
	public int globalBGMVolume = -20;
	
	public SoundBGMPlayer(String... files) {
		musicFiles = new ArrayList<AudioFile>();
		for(String file : files) {
			musicFiles.add(new AudioFile("src/assets/audio/" + file + ".wav"));
		}
	}

	@Override
	public void run() {
		running = true;
		active = true;
		AudioFile song = musicFiles.get(currentSongIndex);
		currentSong = song;
		song.play();
		while (active) {
			while (running) {
				if (!song.isPlaying()) {
					if (++currentSongIndex >= musicFiles.size()) {
						currentSongIndex = 0;
					}
					song = musicFiles.get(currentSongIndex);
					song.play(globalBGMVolume);
				}

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void stop() {
		running = false;
		currentSong.stop();
	}
	
	public void start() {
		running = true;
	}
}
