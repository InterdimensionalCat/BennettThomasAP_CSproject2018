package game.audio;

import java.util.ArrayList;

public class SoundFXManager {

	public volatile ArrayList<SoundFXPlayer> players = new ArrayList<SoundFXPlayer>();
	public volatile int globalFXVolume = -20;
	private volatile int newestPlayer = 0;
	
	public SoundFXManager(int num) {
		for(int i = 0; i < num; i++) {
			players.add(new SoundFXPlayer());
		}
	}
	
	public void playSound(String fx) {
		for(int i = 0; i < players.size(); i++) {
			SoundFXPlayer p = players.get(i);
			if(!p.running) {
  				p.playSound(fx, globalFXVolume);
  				newestPlayer = i;
				return;
			}
		}
		System.err.println("No Players Avalible, flushing players");
		for(int i = 0; i < players.size(); i++) {
			if(i != newestPlayer) {
				players.get(i).stop();
			}
			
			for(int j = 0; j < players.size(); j++) {
				SoundFXPlayer p = players.get(j);
				if(!p.running) {
	  				p.playSound(fx, globalFXVolume);
	  				newestPlayer = j;
					return;
				}
				
			}
		}
	}
}
