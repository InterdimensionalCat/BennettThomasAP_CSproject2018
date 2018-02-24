package game.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.Game;
import game.render.textures.Animation;
import game.render.textures.Texture;
import game.utils.Fonts;
import game.utils.init.InitAnimations;
import game.world.TileMap;

public class EntityBoop extends Mob {
	
	private double displacement;
	private double speed;
	private double spawnPointX;
	private boolean dead;
	private boolean displaced;
	private Animation currentState;

	public EntityBoop(double x, double y, TileMap tileMap, double speed, double displacement) {
		super(new Texture("BoopWalk1"), x, y, tileMap, new Rectangle((int)x, (int)y, 64, 64));
		this.motionX = speed;
		this.speed = speed;
		this.spawnPointX = x;
		this.displacement = displacement;
		this.currentState = InitAnimations.animations.get("Boop_walk");
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
			if (Math.abs(x - spawnPointX) > displacement || displaced) {
				this.motionX = -motionX;
				this.speed = -speed;
				displaced = false;
			}
			currentState.run();
			super.tick();
		}
	}
	
	public void onHit(Player player) {
		player.setDead();
	}
	
	public void onKillHit(Player player) {
		player.setMotionY(-10.0);
		Game.fxmanager.playSound("BoopDeath");
		player.score();
		this.setDead();
	}
	
	@Override
	public void move() {

		if(tileMap.calculateNPCCollision(this, x, y, motionX, motionY)) {
			motionX = -speed;
			speed = -speed;
		}  
		y+= getMotionY();
		x+= getMotionX();
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
