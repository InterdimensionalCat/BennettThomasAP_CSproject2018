package game.utils.init;

import java.util.ArrayList;
import java.util.HashMap;

import game.Game;
import game.render.textures.Animation;
import game.render.textures.Texture;

public class InitAnimations implements Runnable {
	
	public static volatile HashMap<String, Animation> animations = new HashMap<String, Animation>();
	public static volatile ArrayList<String> dashSongs = new ArrayList<String>();
	public static volatile int playerDeath;
	public static volatile int playerScore;

	@Override
	public void run() {
		System.err.println("Loading Animations");
		
		for(int i = 1; i <= 4; i++) {
			dashSongs.add("PlayerDash"+i);
		}
		
		animations.put("Player_idle", new Animation(60, new Texture(new Texture("PlayerIdleMap"), 1, 1, 64, true) ,
				new Texture(new Texture("PlayerIdleMap"), 2, 1, 64, true) ,
				new Texture(new Texture("PlayerIdleMap"), 1, 1, 64, true) ,
				new Texture(new Texture("PlayerIdleMap"), 3, 1, 64, true)));
		
		animations.put("Player_jump", new Animation(10, new Texture(new Texture("PlayerIdleMap"), 4, 1, 64, true) ,
				new Texture(new Texture("PlayerIdleMap"), 1, 2, 64, true) ,
				new Texture(new Texture("PlayerIdleMap"), 2, 2, 64, true) ,
				new Texture(new Texture("PlayerIdleMap"), 3, 2, 64, true)));
		
		animations.put("Player_run", new Animation(dashSongs, 10, /*new Texture(new Texture("PlayerIdleMap"), 1, 1, 64) ,*/
				new Texture(new Texture("PlayerIdleMap"), 4, 2, 64, true),
/*				new Texture(new Texture("PlayerIdleMap"), 1, 3, 64),*/
				new Texture(new Texture("PlayerIdleMap"), 2, 3, 64, true)//,
		/*new Texture(new Texture("PlayerIdleMap"), 1, 3, 64)*/));
		
		animations.put("Player_turnRun", new Animation(10,
				new Texture(new Texture("PlayerIdleMap"), 1, 3, 64, true)));
		
		animations.put("Boop_walk", new Animation(5, new Texture("BoopWalk1"),
				new Texture("BoopWalk2"),
				new Texture("BoopWalk1"),
				new Texture("BoopWalk3")));
		
		System.err.println("Animations Loaded");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Game.driver.getScreen().setProgress((++Game.taskComplete) * 25);
	}

}
