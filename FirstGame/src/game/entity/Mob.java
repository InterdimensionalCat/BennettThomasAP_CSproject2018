package game.entity;


import java.awt.Rectangle;

import game.render.textures.Texture;
import game.world.TileMap;

public abstract class Mob extends Entity { //It means MOBile entity

	protected double motionX;
	protected double motionY;
	protected double gravity;
	protected double maxMotionY;
	protected boolean isAirBorne;
	protected int pushForceY;
	public int tickerAir;
	protected boolean moving;
	protected double lastMoveY;
	protected Rectangle AABB;
	
	public Mob(Texture texture, double x, double y, TileMap tileMap, Rectangle AABB) {
		super(texture, x, y, tileMap, AABB);
		gravity = 0.5;
		maxMotionY = 10;
		tickerAir = 0;
		this.AABB = AABB; 
	}
	
	public abstract void setDead();
	
	@Override
	public void tick() {
		if(getMotionY() == 0) {
			isAirBorne = false;
		} else {
			isAirBorne = true;
		}
		
		fall();
		move();
		if (motionX != 0) {
			//moving = true;
		} else {
			//moving = false;
		}

		if(y > TileMap.tilesToPixels(this.tileMap.getHeight())) {
			this.setDead();
		}
		
	}
	
	public void move() {

		tileMap.calculateCollision(AABB, x, y, motionX, motionY, true);
		y+= getMotionY();
		x+= getMotionX();
	}
	
	
	protected void fall() {
		setMotionY(getMotionY() + gravity);
		if (getMotionY() > maxMotionY ) {
			setMotionY(maxMotionY);
		}
	}
	
	protected void jump(double velocityY) {
		if(!isAirBorne) {
			setMotionY(getMotionY() - velocityY);
			isAirBorne = true;
		}
	}
	
	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}
	
	public void setMotionX(double motionX) { //currently unused
		this.motionX = motionX;
	}
	
	public void setAirBorne(boolean airBorne) {
		this.isAirBorne = airBorne;
	}
	
	public boolean isAirBorne() {
		return isAirBorne;
	}

	public double getMotionY() {
		return motionY;
	}
	
	public double getMotionX() {
		return motionX;
	}
	
	public boolean isMovingLeft() {
		return motionX < 0;
	}
	
	public boolean isMovingRight() {
		return motionX > 0;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
}
