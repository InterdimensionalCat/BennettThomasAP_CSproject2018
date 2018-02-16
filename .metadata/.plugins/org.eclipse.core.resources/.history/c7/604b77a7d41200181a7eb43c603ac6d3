package game.entity;

import java.awt.Rectangle;

import game.render.textures.Texture;
import game.world.TileMap;

public class EntityGoal extends Entity {

	public EntityGoal(Texture texture, double x, double y, TileMap tileMap, Rectangle AABB) {
		super(texture, x, y, tileMap, AABB);
	}

	@Override
	public void tick() {
		if(tileMap.getPlayer().getAABB().intersects(AABB)) {
			System.out.println("You Win!");
		}
		
	}

	@Override
	public void setDead() {
		
	}

}
