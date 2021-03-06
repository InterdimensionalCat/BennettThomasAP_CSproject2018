package game.entity;

import java.awt.Rectangle;

import game.Game;
import game.render.textures.Texture;
import game.world.TileMap;

public class EntityGoal extends Entity {

	public EntityGoal(double x, double y, TileMap tileMap) {
		super(new Texture("EntityGoal"), x, y, tileMap, new Rectangle((int)x, (int)y ,64, 64));
	}

	@Override
	public void tick() {
		if(AABB == null) {
			System.out.println("lmao");
		}
		if(tileMap.getPlayer().getAABB().intersects(AABB)) {
			Game.fxmanager.playSound("PlayerWin");
			Game.pauseTime+= 100;
			Game.level.nextLevel();
			System.out.println("You Win!");
		}
		
	}

	@Override
	public void setDead() {
		AABB = null;
	}

}
