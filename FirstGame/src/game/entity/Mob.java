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
	//public ActionState state;
	public boolean hasCollision = true;
	
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
/*		if(getMotionY() == 0) {
			this.state = ActionState.ON_TILE;
		} else {
			if (motionY > 0) {
				this.state = ActionState.FALLING;
			} else {
				this.state = ActionState.JUMPING;
			}
		}*/
		
		if(motionY != 0) {
			this.isAirBorne = true;
		} else {
			this.isAirBorne = false;
		}
		
		fall();
		move();
/*		if (motionX != 0) {
			moving = true;
		} else {
			moving = false;
		}*/

		if(y > TileMap.convertToPixels(this.tileMap.getHeight())) {
			this.setDead();
		}
		
	}
	
	public void move() {

		//tileMap.calculateCollision(AABB, x, y, motionX, motionY, true);
		tileMap.sonicCollision(x, y, motionX, motionY);
		y+= getMotionY();
		//System.out.println(motionX);
		x+= getMotionX();
	}
	
	
	protected void fall() {
		setMotionY(getMotionY() + gravity);
		if (getMotionY() > maxMotionY ) {
			setMotionY(maxMotionY);
		}
	}
	
	protected void jump(double velocityY) {
		if(true) {
			setMotionY(getMotionY() - velocityY);
			//this.state = ActionState.JUMPING;
			isAirBorne = true;
		}
	}
	
	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}
	
	public void setMotionX(double motionX) {
		//System.out.println(motionX);
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
		//System.out.println(moving);
		this.moving = moving;
	}
	
}
