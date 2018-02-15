package game.utils.init;

import java.util.HashMap;

import game.render.textures.Animation;
import game.render.textures.Texture;

public class InitAnimations implements Runnable {
	
	public static volatile HashMap<String, Animation> animations = new HashMap<String, Animation>();

	@Override
	public void run() {
		System.out.println("Loading Animations");
		
		animations.put("Player_idle", new Animation(60, new Texture(new Texture("PlayerIdleMap"), 1, 1, 64) ,
				new Texture(new Texture("PlayerIdleMap"), 2, 1, 64) ,
				new Texture(new Texture("PlayerIdleMap"), 1, 1, 64) ,
				new Texture(new Texture("PlayerIdleMap"), 3, 1, 64)));
		
		animations.put("Player_jump", new Animation(10, new Texture(new Texture("PlayerIdleMap"), 4, 1, 64) ,
				new Texture(new Texture("PlayerIdleMap"), 1, 2, 64) ,
				new Texture(new Texture("PlayerIdleMap"), 2, 2, 64) ,
				new Texture(new Texture("PlayerIdleMap"), 3, 2, 64)));
		
		animations.put("Player_run", new Animation(10, new Texture(new Texture("PlayerIdleMap"), 1, 1, 64) ,
				new Texture(new Texture("PlayerIdleMap"), 4, 2, 64)));
		
		System.out.println("Animations Loaded");
	}

}
