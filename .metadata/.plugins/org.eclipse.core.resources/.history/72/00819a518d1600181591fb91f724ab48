package game.entity;

import java.awt.Rectangle;

import game.render.textures.Texture;
import game.world.TileMap;

public class Boop extends Mob {

	public Boop(Texture texture, double x, double y, TileMap tileMap, Rectangle AABB) {
		super(texture, x, y, tileMap, AABB);
	}

	@Override
	public void setDead() {
		this.AABB = null;
	}
	
}
