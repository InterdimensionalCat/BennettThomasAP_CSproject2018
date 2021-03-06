package game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.Game;
import game.render.textures.Texture;
import game.world.TileMap;

public abstract class Entity {

	protected double x, y;
	protected Texture texture;
	protected TileMap tileMap;
	protected Rectangle AABB;
	protected boolean collided;
	
	
	public Entity(Texture texture, double x, double y, TileMap tileMap, Rectangle AABB) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		this.tileMap = tileMap;
		this.AABB = AABB;
		tileMap.addEntity(this);
	}
	
	public abstract void tick();
	public abstract void setDead();
	
	public void render(Graphics2D g , int offsetX, int offsetY) {
			texture.render(g, x + offsetX, y + offsetY);
		if (Game.debug) {
			g.setColor(Color.BLUE);
			g.drawRect((int)AABB.getX() + offsetX, (int)AABB.getY() + offsetY, (int)AABB.getWidth(), (int)AABB.getHeight());
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Rectangle getAABB() {
		return AABB;
	}
	
	
/*	public abstract double getMotionY();

	
	public abstract double getMotionX();*/
	

	public boolean isCollided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

	
}
