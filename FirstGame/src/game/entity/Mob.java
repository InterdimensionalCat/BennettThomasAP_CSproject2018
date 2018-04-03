package game.entity;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import javax.sound.sampled.Line;

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
	public boolean falling;
	
	public Line2D floorCheck1;
	public Line2D floorCheck2;
	public Line2D centerLine;
	
	public Point center;
	
	public float angle;
	
	public float gsp;
	
	
	public Mob(Texture texture, double x, double y, TileMap tileMap, Rectangle AABB) {
		super(texture, x, y, tileMap, AABB);
		gravity = 0.5;
		maxMotionY = 10;
		tickerAir = 0;
		this.AABB = AABB;
		
		center = new Point((int)(x+32),(int)(y+32));
		
		floorCheck1 = new Line2D.Float((float)center.getX() + 14, (float)center.getY(), (float)center.getX() + 14, (float)center.getY() + 42);
		floorCheck2 = new Line2D.Float((float)center.getX() - 14, (float)center.getY(), (float)center.getX() - 14, (float)center.getY() + 42);
		centerLine = new Line2D.Float((float)center.getX() - 26, (float)center.getY() + 6, (float)center.getX() + 26, (float)center.getY() + 6);
	}
	
	public void updateLines(double x, double y) {
		center.setLocation((int)(x+32),(int)(y+32));
		
		floorCheck1.setLine((float)center.getX() + 14, (float)center.getY(), (float)center.getX() + 14, (float)center.getY() + 42);
		floorCheck2.setLine((float)center.getX() - 14, (float)center.getY(), (float)center.getX() - 14, (float)center.getY() + 42);
		centerLine.setLine((float)center.getX() - 26, (float)center.getY() + 6, (float)center.getX() + 26, (float)center.getY() + 6);
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
		//tileMap.sonicCollision(x, y, motionX, motionY);
		tileMap.EntityCollision(x,y,motionX,motionY,this);
		y+= getMotionY();
		//System.out.println(motionX);
		x+= getMotionX();
		
		updateLines(x, y);
	}
	
	
	protected void fall() {
		if (falling) {
			setMotionY(getMotionY() + gravity);
			if (getMotionY() > maxMotionY) {
				setMotionY(maxMotionY);
			} 
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
