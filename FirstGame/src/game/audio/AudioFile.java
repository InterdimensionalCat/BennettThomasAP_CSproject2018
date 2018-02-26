package game.audio;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioFile implements LineListener{

	private File soundFile;
	private AudioInputStream stream;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;;
	private FloatControl controller;
	private volatile boolean playing;
	
	public AudioFile(String fileName) {
		try {
			soundFile = new File(fileName);
			stream = AudioSystem.getAudioInputStream(soundFile);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip)AudioSystem.getLine(info);
			clip.addLineListener(this);
			clip.open(stream);
			controller = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	public synchronized void play() {
		play(-20);
	}
	
	public synchronized void play(float volume) {
		controller.setValue(volume);
		clip.start();
		playing = true;
	}
	
	public synchronized boolean isPlaying() {
		return playing;
	}

	@Override
	public void update(LineEvent event) {
		if(event.getType() == LineEvent.Type.START) {
			playing = true;
		} else {
			if(event.getType() == LineEvent.Type.STOP) {
				clip.stop();
				clip.flush();
				clip.setFramePosition(0);
				playing = false;
			}
		}
		
	}
}
