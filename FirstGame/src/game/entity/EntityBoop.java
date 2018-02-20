package game.entity;

import java.awt.Rectangle;

import game.render.textures.Texture;
import game.world.TileMap;

public class EntityBoop extends Mob {
	
	public double displacement;
	public double spawnPointX;
	public boolean dead;

	public EntityBoop(Texture texture, double x, double y, TileMap tileMap, Rectangle AABB, double speed, double displacement) {
		super(texture, x, y, tileMap, AABB);
		this.motionX = speed;
		this.spawnPointX = x;
		this.displacement = displacement;
	}

	@Override
	public void setDead() {
		//this.AABB = null;
/*		this.texture = null;
		tileMap.killEntity(this);*/
		dead = true;
		this.x = -100;
		AABB.setLocation((int)x, (int)y);
		System.out.println("ded");
	}
	
	@Override
	public void tick() {
		if (!dead) {
			if (Math.abs(x - spawnPointX) > displacement) {
				this.motionX = -motionX;
			}
			super.tick();
		}
	}
	
	public void onHit(Player player) {
		player.setDead();
	}
	
	public void onHit() {
		this.setDead();
	}
	
	@Override
	public void move() {

		tileMap.calculateNPCCollision(this, x, y, motionX, motionY);
		y+= getMotionY();
		x+= getMotionX();
	}
}