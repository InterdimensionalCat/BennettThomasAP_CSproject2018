package game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import game.Game;
import game.render.textures.Animation;
import game.render.textures.Texture;
import game.utils.init.InitAnimations;
import game.utils.init.InitAudio;
import game.world.TileMap;

public class EntityBoop extends Mob {
	
	private double displacement;
	private double speed;
	private double spawnPointX;
	private boolean dead;
	private boolean displaced;
	private Animation currentState;
	private boolean jumper;
	private final double acc =  0.046875;
	private final Random xposRand = new Random();

	public EntityBoop(double x, double y, TileMap tileMap, double speed, double displacement, boolean jumper) {
		super(new Texture("BoopWalk1"), x, y, tileMap, new Rectangle((int)x, (int)y, 64, 64));
		this.motionX = speed;
		this.speed = speed;
		this.spawnPointX = x;
		this.displacement = displacement;
		this.currentState = InitAnimations.animations.get("Boop_walk");
		this.jumper = jumper;
		this.abspd = 10.0;
		if(xposRand.nextBoolean()) {
			this.x += xposRand.nextDouble()*displacement;
		} else {
			this.x -= xposRand.nextDouble()*displacement;
		}
		System.out.print("");
		//this.state = ActionState.MOB_AIR_MOVING;
	}
	
	public EntityBoop(double x, double y, TileMap tileMap, double speed, double displacement, boolean jumper, double gravity) {
		this(x, y, tileMap, speed, displacement, jumper);
		this.gravity = gravity;
/*		if(gravity == 0.0) {
			this.state = ActionState.NO_GRAVITY;
		} else {
			this.state = ActionState.MOB_AIR_MOVING;
		}*/
	}

	@Override
	public void setDead() {
		dead = true;
		this.x = -100;
		AABB.setLocation((int)x, (int)y);
	}
	
	@Override
	public void tick() {
		if (!dead) {
/*			if (Math.abs(x - spawnPointX) > displacement || displaced) {
				this.motionX = -motionX;
				this.speed = -speed;
				displaced = false;
			}*/
			

			
			currentState.run();
			super.tick();
			
			
/*			if(motionY != 0 && this.state != ActionState.NO_GRAVITY) {
				this.state = ActionState.MOB_AIR_MOVING;
			}*/
			if(!this.isAirBorne()&&jumper) {
				jump(10);
			}
		}
	}
	
	public void onHit(Player player) {
		player.setDead();
	}
	
	public void onKillHit(Player player) {
		player.setMotionY(-10.0);
		if(!InitAudio.musicFiles.get("BoopDeath").isPlaying()) {
			Game.fxmanager.playSound("BoopDeath");
		} else {
			Game.fxmanager.playSound("BoopDeath2");
		}
		player.score();
		this.setDead();
	}
	
	@Override
	public void move() {

/*		if(tileMap.calculateNPCCollision(this, x, y, motionX, motionY)) {
			motionX = -speed;
			speed = -speed;
		}  
		y+= getMotionY();
		x+= getMotionX();*/
		
/*		if(Math.abs(x - spawnPointX) > displacement*0.7 && !(Math.abs(x - spawnPointX) / (x - spawnPointX) == Math.abs(speed) / speed)) {
			gsp /= 2;
			xsp /= 2;
		}*/
		
		
		if(Math.abs(x - spawnPointX) > displacement*0.7 && !(Math.abs(x - spawnPointX) > displacement)) {
			displaced = false;
		}
		
		if(Math.abs(x - spawnPointX) > displacement && !displaced) {
			speed = -speed;
			//gsp = speed*2;
			//xsp = speed*2;
			gsp = 0;
			xsp = 0;
			displaced = true;
		}
		
		if (falling) {
			xsp += speed * acc;
		} else {
			gsp += speed * acc;
		} 

		super.move();
		
		AABB.setBounds((int)x, (int)y,AABB.width, AABB.height);
	}
	
	@Override
	public void render(Graphics2D g, int offsetX, int offsetY) {
		currentState.render(g, x + offsetX, y + offsetY);
		if (Game.debug) {
			g.setColor(Color.BLUE);
			g.drawRect((int)AABB.getX() + offsetX, (int)AABB.getY() + offsetY, (int)AABB.getWidth(), (int)AABB.getHeight());
		}

	}
}
