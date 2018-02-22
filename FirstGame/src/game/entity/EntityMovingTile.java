package game.entity;

import java.awt.Rectangle;

import game.Game;
import game.render.textures.Animation;
import game.render.textures.Texture;
import game.world.TileMap;

public class EntityMovingTile extends Mob {
	
	private double min;
	private double max;
	private PlatformType type;
	private boolean falling;
	

	public EntityMovingTile(Texture texture, double x, double y, TileMap tileMap, Rectangle AABB, double range, PlatformType type) {
		super(texture, x, y, tileMap, AABB);
		this.type = type;
		if(this.type == PlatformType.VERTICAL_MOVING) {
			this.min = x;
			this.max = x + range;
			motionX = 0.7;
		} else {
			if(this.type == PlatformType.HORIZONTAL_MOVING) {
				this.min = y;
				this.max = y + range;
				motionY = 0.7;
			} else {
				motionY = 0.5;
			}
		}
	}

	@Override
	public void setDead() {
		this.AABB = null;
		this.type = PlatformType.DEAD;
	}

	public void move() {
		if(this.type == PlatformType.VERTICAL_MOVING) {
			if(x + motionX > max || x + motionX < min) {
				motionX = -motionX;
			}
			x += motionX;
			AABB.setBounds((int)x, (int)y, (int)AABB.getWidth(), (int)AABB.getHeight());

		}
		
		if(this.type == PlatformType.HORIZONTAL_MOVING) {
			if(y + motionY > max || y + motionY < min) {
				motionY = -motionY;
			}
			y += motionY;
			AABB.setBounds((int)x, (int)y, (int)AABB.getWidth(), (int)AABB.getHeight());

		}
		
		if(this.type == PlatformType.FALLING && falling) {
			//this.motionY *= 1.01;
			this.motionY += 0.5;
			if(this.motionY > 10) {
				this.motionY = 10;
			}
		    y += motionY;
		    //AABB.setBounds((int)x, (int)y, (int)AABB.getHeight(), (int)AABB.getWidth());
		}
		if(this.type != PlatformType.DEAD) {
		    AABB.setBounds((int)x, (int)y, (int)AABB.getWidth(), (int)AABB.getHeight());
		}
	}
	
	public void tick() {
		move();
		if(collided) {
			falling = true;
		}
		collided = false;
		if(this.y - 100 > Game.HEIGHT) {
			this.setDead();
		}
		
	}
	
	public PlatformType getPlatformType() {
		return type;
	}
	
	/*public void fall() {
		if(this.type == PlatformType.FALLING) {
			falling = true;
			this.motionY += motionY;
		    y += motionY;
		    AABB.setBounds((int)x, (int)y, (int)AABB.getHeight(), (int)AABB.getWidth());
		}
	}*/
	
}
