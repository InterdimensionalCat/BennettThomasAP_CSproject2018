package game.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import game.input.KeyInput;
import game.render.textures.Animation;
import game.render.textures.Texture;
import game.utils.init.InitAnimations;
import game.world.TileMap;

public class Player extends Mob {
	
	private int playerSpawnX;
	private int playerSpawnY;
	private TileMap tileMap;
	private double maxMotionX;
	private Animation idle;

	public Player(double x, double y, TileMap tileMap) {
		super(new Texture(new Texture("PlayerIdleMap"), 1, 1, 64), x, y, tileMap,new Rectangle());


		this.AABB = new Rectangle((int)this.getCollisionX(),(int)this.getCollisionY(),this.getCollisionWidth(),this.getCollisionHeight());
		this.tileMap = tileMap;
		this.maxMotionX = 6.0;
		this.idle = InitAnimations.animations.get("Player_idle"); 
	}
	
	@Override
	public void render(Graphics2D g, int offsetX, int offsetY) {
		idle.render(g, x + offsetX, y + offsetY);
	}

	@Override
	public void tick() {

		
		if(KeyInput.isDown(KeyEvent.VK_A)) {
			moving = true;
			if(isAirBorne) {
				motionX -= 6.0;
				if(motionX < -maxMotionX) {
					motionX = -maxMotionX;
				}
			} else {
				motionX -= 0.3;
				if(motionX < -maxMotionX) {
					motionX = -maxMotionX;
				}
			}
		}
		
		
		
		if(KeyInput.isDown(KeyEvent.VK_D)) {
			moving = true;
			if(isAirBorne) {
				motionX += 6.0;
				if(motionX > maxMotionX) {
					motionX = maxMotionX;
				}
			} else {
				motionX += 0.3;
				if(motionX > maxMotionX) {
					motionX = maxMotionX;
				}
			}
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_SPACE)) {
			jump(15.0);
		}
		
		if(KeyInput.wasReleased(KeyEvent.VK_A)||KeyInput.wasReleased(KeyEvent.VK_D)) {
			//motionX /= 4;
			moving = false;
		}
		
		if(KeyInput.wasReleased(KeyEvent.VK_SPACE) && motionY < 0) {
			motionY /= 1.5;
		}
		
		if(!moving) {
			idle.run();
			if(motionX < 1 && motionX > -1) {
				motionX = 0;
			} else {
				if(motionX > 0) {
					//motionX -= 0.6;
					motionX /= 1.4;
				}
				if(motionX < 0) {
					//motionX += 0.6;
					motionX /= 1.4;
				}
			}
			if(isAirBorne) {
				motionX = 0;
			}
		}
		super.tick();
	}
	
	public int getCollisionWidth() {
		return texture.getWidth() - 24;
	}
	
	public double getCollisionX() {
		return x + 5;
	}
	
	public double ajustXforCollision(double x) {
		return x + 5;
	}
	
	public double ajustYforCollision(double y) {
		return y + 10;
	}
	
	public double AABBtoX(double x) {
		return x - 5;
	}
	
	public double AABBtoY(double y) {
		return y - 10;
	}
	
	
	public int getCollisionHeight() {
		return texture.getHeight() - 10;
	}
	
	public double getCollisionY() {
		return y + 10;
	}


	public Rectangle getAABB() {
		return AABB;
	}
	
	public void setDead() {
		System.out.println("You Died!");
		x = playerSpawnX;
		y = playerSpawnY;	
	}
	
	public int getPlayerSpawnX() {
		return playerSpawnX;
	}
	
	public int getPlayerSpawnY() {
		return playerSpawnY;
	}
	
	public void setPlayerSpawnX(int x) {
		playerSpawnX = x;
	}
	
	public void setPlayerSpawnY(int y) {
		playerSpawnY = y;
	}
}
