package game.utils.init;

import java.util.HashMap;

import game.Game;
import game.render.textures.Texture;

public class InitTextures implements Runnable {
	
	public static volatile HashMap<String, Texture> textures = new HashMap<String, Texture>();

	@Override
	public void run() {
		System.err.println("Loading Textures");
		
		addTexture(new Texture("SpriteMap1"));
		addTexture(new Texture("PlayerIdleMap"));
/*		addTexture(new Texture("background13"));
		addTexture(new Texture("background2"));
		addTexture(new Texture("background32"));*/
		addTexture(new Texture("BoopWalk1"));
		addTexture(new Texture("BoopWalk2"));
		addTexture(new Texture("BoopWalk3"));
		addTexture(new Texture("EntityGoal"));
		
		System.err.println("Textures Loaded");
		Game.driver.getScreen().setProgress((++Game.taskComplete) * 25);
	}

	public static void addTexture(Texture t) {
		textures.put(t.getName(), t);
		System.out.println(t.getName() + " Added");
	}
	
}
