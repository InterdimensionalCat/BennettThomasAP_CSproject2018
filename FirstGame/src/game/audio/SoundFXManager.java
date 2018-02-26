package game.audio;

import java.util.ArrayList;

public class SoundFXManager {

	public volatile ArrayList<SoundFXPlayer> players = new ArrayList<SoundFXPlayer>();
	public volatile int globalFXVolume = -20;
	
	public SoundFXManager(int num) {
		for(int i = 0; i < num; i++) {
			players.add(new SoundFXPlayer());
		}
	}
	
	public void playSound(String fx) {
		for(SoundFXPlayer p : players) {
			if(!p.running) {
  				p.playSound(fx, globalFXVolume);
				return;
			}
		}
		System.err.println("No Players Avalible");
	}
}
