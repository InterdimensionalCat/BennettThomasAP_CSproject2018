package game.utils.init;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import game.entity.Entity;
import game.entity.EntityGoal;
import game.entity.EntityMovingTile;
import game.entity.PlatformType;
import game.render.textures.Texture;
import game.world.TileMap;

public class PreloadLevels implements Runnable {

	public static volatile ArrayList<String> levels = new ArrayList<String>();
	public static volatile HashMap<String , Entity[]> levelEntities = new HashMap<String, Entity[]>();
	public static volatile TileMap tileMap = new TileMap("level_4");
	public static volatile Entity[] entityLevel1 = {
			(Entity)new EntityMovingTile(new Texture("movingTile"), 1000, 300, tileMap, new Rectangle(100, 100, 128, 20), 200, PlatformType.VERTICAL_MOVING),
			(Entity)new EntityMovingTile(new Texture("movingTile"), 100, 500, tileMap, new Rectangle(100, 100, 128, 20), 100, PlatformType.HORIZONTAL_MOVING),
			(Entity)new EntityMovingTile(new Texture("movingTile"), 2300, 500, tileMap, new Rectangle(100, 100, 128, 20), 100, PlatformType.FALLING),
			(Entity)new EntityMovingTile(new Texture("movingTile"), 1500, 300, tileMap, new Rectangle(100, 100, 128, 20), 100, PlatformType.FALLING),
			(Entity)new EntityGoal(new Texture(new Texture("SpriteMap1"), 4, 1 , 64, 64), 2000.0, 400.0, tileMap, new Rectangle(2000, 400, 64, 64))
		};
	
	public static volatile Entity[] entityLevel2 = {
			(Entity)new EntityGoal(new Texture(new Texture("SpriteMap1"), 4, 1 , 64, 64), 0.0, 0.0, tileMap, new Rectangle(0, 0, 64, 64))
		};
	
	@Override
	public void run() {
		System.out.println("Preloading Levels");
		levels.add("level_4");
		levelEntities.put("level_4", entityLevel1);
		
		levels.add("level_3");

		levelEntities.put("level_3", entityLevel2);
		
		System.out.println("Levels Loaded");
	}
}