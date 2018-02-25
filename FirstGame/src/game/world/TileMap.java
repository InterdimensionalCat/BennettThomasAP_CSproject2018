package game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.Game;
import game.entity.Entity;
import game.entity.EntityBoop;
import game.entity.EntityMovingTile;
import game.entity.Mob;
import game.entity.PlatformType;
import game.entity.Player;
import game.render.ParallaxEngine;
import game.render.ParallaxLayer;
import game.render.textures.Texture;

public class TileMap {
	
	private String name;
	private int width, height;
	private Tile[] tiles; //MAKE THIS A DOUBLE ARRAY BOI
	
	private static final int TILE_SIZE = 64;
	private static final int TILE_SIZE_BITS = 6;

	private Player player;
	private ParallaxEngine parallaxEngine;
	private ArrayList<Entity> entities;
	
	private boolean isOnMovingTile;

	public TileMap(String name) {
		entities = new ArrayList<Entity>();
		ParallaxLayer layer1 = new ParallaxLayer(new Texture("background13"), (int)((16 * 0.25) * -0.23));
		ParallaxLayer layer2 = new ParallaxLayer(new Texture("background2"), (int)((16 * 0.25) * -0.3));
		ParallaxLayer layer3 = new ParallaxLayer(new Texture("background32"), (int)((16 * 0.25) * -0.6));
		this.parallaxEngine = new ParallaxEngine(layer1, layer2, layer3);
		load(name);
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || x>= width || y < 0 || y >= height) {
			return null;
		} else {
			return tiles[x + y * width];
		}
	}
	
	public void setTile(int x, int y, Tile tile) {
		tiles[x + y *width] = tile; //proper math?
	}
	
	public static int pixelsToTiles(int pixel) {
		return (int)Math.round(pixel / 64); // this can be done with *return pixel >> TILE_SIZE_BITS;* but it will truncate as opposed to round, so this is better
	}
	
	public static int tilesToPixels(int tile) {
		return tile << TILE_SIZE_BITS;
	}
	
	public void tick() {
		player.tick();
		if(player.isMovingLeft()) {
			parallaxEngine.setLeft();
		} else {
			if(player.isMovingRight()) {
				parallaxEngine.setRight();
		    }
		}
		if(player.isMoving()) {
			parallaxEngine.setMove(player.getMotionX());
		}
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		//player.tick();
	}
	
	public void render(Graphics2D g2d, int screenWidth, int screenHeight) {

		int mapWidth = tilesToPixels(width);
		int mapHeight = tilesToPixels(height);
		int offsetX =(int)(screenWidth/2 - player.getX() - TILE_SIZE /2);
		int offsetY =(int) (screenHeight/2 - player.getY() - TILE_SIZE /2);
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, screenWidth - mapWidth);
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, screenHeight - mapHeight);
		
		int firstX = pixelsToTiles(-offsetX);
		int lastX = firstX + pixelsToTiles(screenWidth) + 1;
		int firstY = pixelsToTiles(-offsetY);
		int lastY = firstY + pixelsToTiles(screenHeight) + 1;
		
		parallaxEngine.render(g2d);
		
		for(int y = firstY; y <= lastY; y++) {
			for(int x = firstX; x <= lastX; x++) {
				Tile t = getTile(x , y);
				if (t != null) {
					t.render(g2d, tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY);
				}
				if (Game.showTileMap) {
					g2d.setColor(Color.RED);
					g2d.drawRect(tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY, 64, 64);
				}
			}	
		}
		
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g2d, offsetX, offsetY);
		}
		player.render(g2d, offsetX, offsetY);
		if(Game.debug) {
			g2d.setColor(Color.GREEN);
			if(player.getAABB() != null) {
				g2d.drawRect((int)player.getAABB().getX() + offsetX, (int)player.getAABB().getY() + offsetY, (int)player.getAABB().getWidth(), (int)player.getAABB().getHeight());
			}
			
		}
	}
	
	
	public void calculateCollision(Rectangle AABB, double posX, double posY, double motionX, double motionY) {
		double toX = posX + motionX;
		double toY = posY + motionY;
		double newX = -1.0;
		AABB.setBounds((int)player.ajustXforCollision(toX), (int)player.ajustYforCollision(posY), AABB.width, AABB.height);
		
		
		
		if(getTile(pixelsToTiles((int)AABB.getMaxX()), pixelsToTiles((int)AABB.getMinY() + 1)) != null) { //this is the top right corner
			Tile tile = getTile(pixelsToTiles((int)AABB.getMaxX()), pixelsToTiles((int)AABB.getMinY() + 1));
			if (tile.type == TileType.SOLID) { 
				newX = tilesToPixels(pixelsToTiles((int)AABB.getMaxX())) - AABB.getWidth() - 1 - 5;
				player.setX(newX);
				AABB.setBounds((int)player.ajustXforCollision(newX), (int)player.ajustYforCollision(posY), AABB.width, AABB.height);
				player.setMotionX(0);
				player.setMoving(false);
				
				if(Game.debug) {
					System.out.println("Collided top right horizontally");
				}
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {
					newX = toX;
					if(Game.debug) {
						System.out.println("Walking up a slope");
					}
				}
			}
		}
		
		if(getTile(pixelsToTiles((int)AABB.getMaxX()), pixelsToTiles((int)AABB.getMaxY() - 1)) != null) { //this is the bottom right corner
			Tile tile = getTile(pixelsToTiles((int)AABB.getMaxX()), pixelsToTiles((int)AABB.getMaxY() - 1));
			if (tile.type == TileType.SOLID) {
				newX = tilesToPixels(pixelsToTiles((int) AABB.getMaxX())) - AABB.getWidth() - 1 - 5;
				player.setX(newX);
				AABB.setBounds((int) player.ajustXforCollision(newX), (int) player.ajustYforCollision(posY), AABB.width,
						AABB.height);
				player.setMotionX(0);
				player.setMoving(false);
				if(Game.debug) {
					System.out.println("Collided bottom right horizontally");
				}
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {
					newX = toX;
					//double bottomSlope = tilesToPixels(pixelsToTiles((int) AABB.getMaxX()));
					
					player.setY(tilesToPixels(pixelsToTiles((int)(int)player.ajustYforCollision(posY) - 1)) + (64 - toX % 64));
					if(Game.debug) {
						System.out.println("Walking up a slope");
					}
				}
			}
		}
		
		if(getTile(pixelsToTiles((int)AABB.getMinX()), pixelsToTiles((int)AABB.getMinY() + 1)) != null) { //this is the top left corner
			newX = tilesToPixels(pixelsToTiles((int)AABB.getMaxX())) - 5/*this is the offset from AABB hitbox to texture*/ ;
			player.setX(newX);
			AABB.setBounds((int)player.ajustXforCollision(newX), (int)player.ajustYforCollision(posY), AABB.width, AABB.height);
			player.setMotionX(0);
			player.setMoving(false);
			
			if(Game.debug) {
				System.out.println("Collided top left horizontally");
			}
			
		}
		
		if(getTile(pixelsToTiles((int)AABB.getMinX()), pixelsToTiles((int)AABB.getMaxY()  - 1)) != null && !isOnMovingTile) { //this is the bottom left corner
			newX = tilesToPixels(pixelsToTiles((int)AABB.getMaxX())) - 5/*this is the offset from AABB hitbox to texture*/ ;
			player.setX(newX);
			AABB.setBounds((int)player.ajustXforCollision(newX), (int)player.ajustYforCollision(posY), AABB.width, AABB.height);
			player.setMotionX(0);
			player.setMoving(false);
			
			if(Game.debug) {
				System.out.println("Collided bottom left horizontally");
			}
		}
		
		if (newX == -1.0) {
			newX = toX;
		}
		
		if((int)newX < 0) { //colliding with the map boundry
			player.setX(1);
			player.setMotionX(0);
			player.setMoving(false);
		}
		
		if((int)newX + 64 > tilesToPixels(this.width)) {
			player.setX(tilesToPixels(this.width) - 64); // must change this
			player.setMotionX(0);
			player.setMoving(false);
		}
		
		//horz is first, now vert;
		
		AABB.setBounds((int)player.ajustXforCollision(newX), (int)player.ajustYforCollision(toY), AABB.width, AABB.height);
		
		if(getTile(pixelsToTiles((int)AABB.getMaxX() - 1), pixelsToTiles((int)AABB.getMinY())) != null) { // this is the top right corner
			player.setMotionY(motionY / 4);
			
			if(Game.debug) {
				System.out.println("Collided top left vertically");
			}
			
		}
			
		if(getTile(pixelsToTiles((int)AABB.getMinX() + 1), pixelsToTiles((int)AABB.getMinY())) != null) { // this is the top left corner
			player.setMotionY(motionY / 4);
			
			if(Game.debug) {
				System.out.println("Collided top right vertically");
			}
			
		}
		
		
		if(getTile(pixelsToTiles((int)AABB.getMaxX() - 1), pixelsToTiles((int)AABB.getMaxY())) != null && !isOnMovingTile) { // this is the bottom right corner
			Tile tile = getTile(pixelsToTiles((int)AABB.getMaxX() - 1), pixelsToTiles((int)AABB.getMaxY()));
			if(tile.type == TileType.SOLID) {
				player.setY(tilesToPixels(pixelsToTiles((int)player.ajustYforCollision(posY))));
				player.setMotionY(0);
				player.setAirBorne(false);
				
				if(Game.debug) {
					System.out.println("Collided bottom right vertically");
				}
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {
					//player.setY(tilesToPixels(pixelsToTiles((int)(int)player.ajustYforCollision(posY))) - 64 + toX % 64);
					player.setY(tilesToPixels(pixelsToTiles((int)(int)player.ajustYforCollision(posY))) + (64 - toX % 64));
					player.setMotionY(0);
					player.setAirBorne(false);
					if(Game.debug) {
						System.out.println("Still walking up a slope");
					}
				}
			}
		}
		
		if(getTile(pixelsToTiles((int)AABB.getMinX() + 1), pixelsToTiles((int)AABB.getMaxY())) != null) { // this is the bottom left corner
			Tile tile = getTile(pixelsToTiles((int)AABB.getMinX() + 1), pixelsToTiles((int)AABB.getMaxY()));
			if(tile.type == TileType.SOLID) {
				player.setY(tilesToPixels(pixelsToTiles((int)(int)player.ajustYforCollision(posY))));
				player.setMotionY(0);
				player.setAirBorne(false);
				if(Game.debug) {
					System.out.println("Collided bottom left vertically");
				}
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {
					//player.setY(tilesToPixels(pixelsToTiles((int)(int)player.ajustYforCollision(posY))) - 64 + toX % 64);
					player.setY(tilesToPixels(pixelsToTiles((int)(int)player.ajustYforCollision(posY))) + (64 - toX % 64));
					player.setMotionY(0);
					player.setAirBorne(false);
					if(Game.debug) {
						System.out.println("Still walking up a slope");
					}
				}
			}
		}

		isOnMovingTile = false;
		
		//Entity Collision
		for (Entity e : entities) {
			if (e instanceof EntityMovingTile) {
				EntityMovingTile movingTile = (EntityMovingTile)e;
				
				if (player.getAABB().getMaxY() > movingTile.getAABB().getMinY()
						&& player.getAABB().getMaxY() < movingTile.getAABB().getMaxY()
						&& player.getAABB().getMaxX() > movingTile.getAABB().getMinX()
						&& player.getAABB().getMaxX() < movingTile.getAABB().getMaxX()
						&& player.getY() + 60 <= movingTile.getAABB().getMinY()) { // this is the bottom right corner

					player.setY(movingTile.getAABB().getMinY() - 62);
					player.setMotionY(0);
					player.setAirBorne(false);
					movingTile.setCollided(true);
					isOnMovingTile = true;
					
					if(movingTile.getMotionY()  > 0) {
						player.setY(movingTile.getAABB().getMinY() - 62);
					}
					
					if(!player.isMoving()/* && movingTile.getPlatformType() == PlatformType.HORIZONTAL_MOVING*/) {
						//player.setMotionX(movingTile.getMotionX()*2.4); //2.4 is a constant that prevents traction from slowing down the player
						player.setX(player.getX() + movingTile.getMotionX());
						//player.setMotionY(movingTile.getMotionY());
					}

					if (Game.debug) {
						System.out.println("Collided bottom left with an entity vertically");
					}

				} else {
					if (player.getAABB().getMaxY() > movingTile.getAABB().getMinY()
							&& player.getAABB().getMaxY() < movingTile.getAABB().getMaxY()
							&& player.getAABB().getMinX() < movingTile.getAABB().getMaxX()
							&& player.getAABB().getMinX() > movingTile.getAABB().getMinX()
							&& player.getY() + 60 <= movingTile.getAABB().getMinY()) { // this is the bottom left corner

						player.setY(movingTile.getAABB().getMinY() - 62);
						player.setMotionY(0);
						player.setAirBorne(false);
						movingTile.setCollided(true);
						isOnMovingTile = true;
						
						if(movingTile.getMotionY()  > 0) {
							player.setY(movingTile.getAABB().getMinY() - 62);
						}
						
						if(!player.isMoving()/* && movingTile.getPlatformType() == PlatformType.HORIZONTAL_MOVING*/) {
							player.setMotionX(movingTile.getMotionX()*2.4); //2.4 is a constant that prevents traction from slowing down the player
							player.setMotionY(movingTile.getMotionY());
						}
						
						if(movingTile.getPlatformType() == PlatformType.FALLING) {
							//player.setY(movingTile.getY() + movingTile.getMotionY());
							player.setMotionY(movingTile.getMotionY());
						}

						if (Game.debug) {
							System.out.println("Collided bottom right with an entity vertically");
						}
					}
				}
			}
		}
	}
	
	public boolean calculateNPCCollision(Mob mob, double posX, double posY, double motionX, double motionY) {
		double toX = posX + motionX;
		double toY = posY + motionY;
		double newX = -1.0;
		mob.getAABB().setBounds((int)toX, (int)posY, mob.getAABB().width, mob.getAABB().height);
		
		
		
		if(getTile(pixelsToTiles((int)mob.getAABB().getMaxX()), pixelsToTiles((int)mob.getAABB().getMinY() + 1)) != null) { //this is the top right corner
			Tile tile = getTile(pixelsToTiles((int)mob.getAABB().getMaxX()), pixelsToTiles((int)mob.getAABB().getMinY() + 1));
			if (tile.type == TileType.SOLID) { 
				newX = tilesToPixels(pixelsToTiles((int)mob.getAABB().getMaxX())) - mob.getAABB().getWidth() - 1 - 5;
				mob.setX(newX);
				mob.getAABB().setBounds((int)newX, (int)posY, mob.getAABB().width, mob.getAABB().height);
				mob.setMotionX(0);
				mob.setMoving(false);
				if(Game.debug) {
					System.out.println("Collided top right horizontally");
				}
				
				return true;
				
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {
					newX = toX;
					if(Game.debug) {
						System.out.println("Walking up a slope");
					}
				}
			}
		}
		
		if(getTile(pixelsToTiles((int)mob.getAABB().getMaxX()), pixelsToTiles((int)mob.getAABB().getMaxY() - 1)) != null) { //this is the bottom right corner
			Tile tile = getTile(pixelsToTiles((int)mob.getAABB().getMaxX()), pixelsToTiles((int)mob.getAABB().getMaxY() - 1));
			if (tile.type == TileType.SOLID) {
				newX = tilesToPixels(pixelsToTiles((int) mob.getAABB().getMaxX())) - mob.getAABB().getWidth() - 1 - 5;
				mob.setX(newX);
				mob.getAABB().setBounds((int)newX, (int)posY, mob.getAABB().width, mob.getAABB().height);
				mob.setMotionX(0);
				mob.setMoving(false);
				if(Game.debug) {
					System.out.println("Collided bottom right horizontally");
				}
				
				return true;
				
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {

				}
			}
		}
		
		if(getTile(pixelsToTiles((int)mob.getAABB().getMinX()), pixelsToTiles((int)mob.getAABB().getMinY() + 1)) != null) { //this is the top left corner
			newX = tilesToPixels(pixelsToTiles((int)mob.getAABB().getMaxX())) - 5/*this is the offset from mob.getAABB() hitbox to texture*/ + 1;
			mob.setX(newX);
			mob.getAABB().setBounds((int)newX, (int)posY, mob.getAABB().width, mob.getAABB().height);
			mob.setMotionX(0);
			mob.setMoving(false);
			
			if(Game.debug) {
				System.out.println("Collided top left horizontally");
			}
			
			return true;
			
		}
		
		if(getTile(pixelsToTiles((int)mob.getAABB().getMinX()), pixelsToTiles((int)mob.getAABB().getMaxY()  - 1)) != null && !isOnMovingTile) { //this is the bottom left corner
			newX = tilesToPixels(pixelsToTiles((int)mob.getAABB().getMaxX())) - 5/*this is the offset from mob.getAABB() hitbox to texture*/ + 1;
			mob.setX(newX);
			mob.getAABB().setBounds((int)newX, (int)posY, mob.getAABB().width, mob.getAABB().height);
			mob.setMotionX(0);
			mob.setMoving(false);
			
			if(Game.debug) {
				System.out.println("Collided bottom left horizontally");
			}
			
			return true;
			
		}
		
		if (newX == -1.0) {
			newX = toX;
		}
		
		if((int)newX < 0) { //colliding with the map boundry
			mob.setX(1);
			mob.setMotionX(0);
			mob.setMoving(false);
		}
		
		if((int)newX + 64 > tilesToPixels(this.width)) {
			mob.setX(tilesToPixels(this.width) - 64); // must change this
			mob.setMotionX(0);
			mob.setMoving(false);
		}
		
		//horz is first, now vert;
		
		mob.getAABB().setBounds((int)newX, (int)toY, mob.getAABB().width, mob.getAABB().height);
		
		if(getTile(pixelsToTiles((int)mob.getAABB().getMaxX() - 1), pixelsToTiles((int)mob.getAABB().getMinY())) != null) { // this is the top right corner
			mob.setMotionY(motionY / 4);
			
			if(Game.debug) {
				System.out.println("Collided top left vertically");
			}
			
		}
			
		if(getTile(pixelsToTiles((int)mob.getAABB().getMinX() + 1), pixelsToTiles((int)mob.getAABB().getMinY())) != null) { // this is the top left corner
			mob.setMotionY(motionY / 4);
			
			if(Game.debug) {
				System.out.println("Collided top right vertically");
			}
			
		}
		
		
		if(getTile(pixelsToTiles((int)mob.getAABB().getMaxX() - 1), pixelsToTiles((int)mob.getAABB().getMaxY())) != null && !isOnMovingTile) { // this is the bottom right corner
			Tile tile = getTile(pixelsToTiles((int)mob.getAABB().getMaxX() - 1), pixelsToTiles((int)mob.getAABB().getMaxY()));
			if(tile.type == TileType.SOLID) {
				mob.setY(tilesToPixels(pixelsToTiles((int)posY + 10)));
				mob.setMotionY(0);
				mob.setAirBorne(false);
				
				if(Game.debug) {
					System.out.println("Collided bottom right vertically");
				}
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {

				}
			}
		}
		
		if(getTile(pixelsToTiles((int)mob.getAABB().getMinX() + 1), pixelsToTiles((int)mob.getAABB().getMaxY())) != null) { // this is the bottom left corner
			Tile tile = getTile(pixelsToTiles((int)mob.getAABB().getMinX() + 1), pixelsToTiles((int)mob.getAABB().getMaxY()));
			if(tile.type == TileType.SOLID) {
				mob.setY(tilesToPixels(pixelsToTiles((int)posY + 10)));
				mob.setMotionY(0);
				mob.setAirBorne(false);
				if(Game.debug) {
					System.out.println("Collided bottom left vertically");
				}
			} else {
				if(tile.type == TileType.SLOPE_RIGHT_64_00) {
					
				}
			}
		}

		isOnMovingTile = false;
		
		//Entity Collision
		for (Entity e : entities) {
			if (e instanceof EntityMovingTile) {
				EntityMovingTile movingTile = (EntityMovingTile)e;
				
				if (mob.getAABB().getMaxY() > movingTile.getAABB().getMinY()
						&& mob.getAABB().getMaxY() < movingTile.getAABB().getMaxY()
						&& mob.getAABB().getMaxX() > movingTile.getAABB().getMinX()
						&& mob.getAABB().getMaxX() < movingTile.getAABB().getMaxX()
						&& mob.getY() + 60 <= movingTile.getAABB().getMinY()) { // this is the bottom right corner

					mob.setY(movingTile.getAABB().getMinY() - 62);
					mob.setMotionY(0);
					mob.setAirBorne(false);
					movingTile.setCollided(true);
					isOnMovingTile = true;
					
					if(movingTile.getMotionY()  > 0) {
						mob.setY(movingTile.getAABB().getMinY() - 62);
					}
					
					if(!mob.isMoving()/* && movingTile.getPlatformType() == PlatformType.HORIZONTAL_MOVING*/) {
						//player.setMotionX(movingTile.getMotionX()*2.4); //2.4 is a constant that prevents traction from slowing down the player
						mob.setX(mob.getX() + movingTile.getMotionX());
						//player.setMotionY(movingTile.getMotionY());
					}

					if (Game.debug) {
						System.out.println("Collided bottom left with an entity vertically");
					}

				} else {
					if (mob.getAABB().getMaxY() > movingTile.getAABB().getMinY()
							&& mob.getAABB().getMaxY() < movingTile.getAABB().getMaxY()
							&& mob.getAABB().getMinX() < movingTile.getAABB().getMaxX()
							&& mob.getAABB().getMinX() > movingTile.getAABB().getMinX()
							&& mob.getY() + 60 <= movingTile.getAABB().getMinY()) { // this is the bottom left corner

						mob.setY(movingTile.getAABB().getMinY() - 62);
						mob.setMotionY(0);
						mob.setAirBorne(false);
						movingTile.setCollided(true);
						isOnMovingTile = true;
						
						if(movingTile.getMotionY()  > 0) {
							mob.setY(movingTile.getAABB().getMinY() - 62);
						}
						
						if(!mob.isMoving()/* && movingTile.getPlatformType() == PlatformType.HORIZONTAL_MOVING*/) {
							mob.setMotionX(movingTile.getMotionX()*2.4); //2.4 is a constant that prevents traction from slowing down the player
							mob.setMotionY(movingTile.getMotionY());
						}

						if (Game.debug) {
							System.out.println("Collided bottom right with an entity vertically");
						}
					}
				}
			} else {
				if (e instanceof EntityBoop) {
					EntityBoop boop = (EntityBoop)e;
					if (player.getAABB().intersects(boop.getAABB())) {
						if (boop.getY() - player.getY() < 48) {
							boop.onHit(player);
						} else {
							boop.onKillHit(player);
						}
					}
				}
			}
		}
		return false;
	}
	
	public void load(String name) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/assets/levels/" + name + ".png"));
			System.out.println("read: " + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.name = name;
		this.width = image.getWidth();
		this.height = image.getHeight();
		tiles = new Tile[width * height];
		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int id = pixels[x + y * width];
				if(id == 0xFF0000FF) {
					player = new Player(tilesToPixels(x), tilesToPixels(y), this);
					player.setPlayerSpawnX(tilesToPixels(x));
					player.setPlayerSpawnY(tilesToPixels(y));
				} else {
					if(Tile.getFromID(id) != null) {
						setTile(x, y, Tile.getFromID(id));
					}
			
				}
		
			}
/*			EntityMovingTile movingTile = new EntityMovingTile(new Texture("movingTile"), 1000, 300, this, new Rectangle(100, 100, 128, 20), 200, PlatformType.VERTICAL_MOVING);
			EntityMovingTile movingTile1 = new EntityMovingTile(new Texture("movingTile"), 100, 500, this, new Rectangle(100, 100, 128, 20), 100, PlatformType.HORIZONTAL_MOVING);
			EntityMovingTile movingTile2 = new EntityMovingTile(new Texture("movingTile"), 1500, 300, this, new Rectangle(100, 100, 128, 20), 100, PlatformType.FALLING);
			EntityGoal goal = new EntityGoal(new Texture(new Texture("SpriteMap1"), 4, 1 , 64, 64), 1000.0, 400.0, this, new Rectangle(1000, 400, 64, 64));
			addEntity(movingTile);
			addEntity(movingTile1);
			addEntity(movingTile2);
			addEntity(goal);*/
	
		}

	}
	
	public void unload() {
		for(Entity e: entities) {
			e.setDead();
			e = null;
		}
		entities.clear();
		
	}
	
	public void setEntityList(Entity[] arr) {
		entities.clear();
		for(Entity e : arr) {
			entities.add(e);
		}
	}
	
	public void addEntity(Entity e) {
		if(! (e instanceof Player)) {
			entities.add(e);
		}
	}
	
	public void removeEntity(Entity e) {
		if(! (e instanceof Player)) {
			entities.remove(e);
		}
	}
	
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}	
		
	public Player getPlayer() {
		return player;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	public void killEntity(Entity e) {
		removeEntity(e);
		//e = null;
	}
		
}
