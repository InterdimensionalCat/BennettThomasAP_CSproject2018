package game.states;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.Game;
import game.entity.Entity;
import game.entity.Player;
import game.render.textures.Texture;
import game.world.Tile;
import game.world.TileMap;

public class GameState implements State {
	
	//private ArrayList<Entity> entities;
	//private ArrayList<Tile> tiles;
	//private World world;
	
	private TileMap tileMap;

	@Override
	public void init() {
		tileMap = new TileMap("level_4");
		//entities = new ArrayList<Entity>();
		//tiles = new ArrayList<Tile>();
		//new Player(new Sprite("Player"), 100, 100, this);
		//world = new World("Level_1", this);
/*		float x = 0;
		float y = GandW.HEIGHT - 64;

		for (int i = 0; i < 20; i++) {
			tiles.add(new Tile(x, y, new Sprite(new SpriteSheet(new Texture("SpriteMap1") , 64), 3, 1)));
			x+=64;
		}
		
		tiles.add(new Tile(GandW.WIDTH - 64, y-64, new Sprite(new SpriteSheet(new Texture("SpriteMap1") , 64), 3, 1)));
		tiles.add(new Tile(GandW.WIDTH - 64, y-256, new Sprite(new SpriteSheet(new Texture("SpriteMap1") , 64), 3, 1)));
		tiles.add(new Tile(64, y-256, new Sprite(new SpriteSheet(new Texture("SpriteMap1") , 64), 3, 1)));*/
	}

	@Override
	public void enter() {
		
	}

	@Override
	public void tick(StateManager stateManager) {
/*		for(Entity e : entities) {
			e.tick();
		}*/
		tileMap.tick();
	}

	@Override
	public void render(Graphics2D g) {
/*		for(Entity e : entities) {
			e.render(g);
		}
		
		for(Tile t : tiles) {
			t.render(g);
		}*/
		tileMap.render(g, Game.WIDTH, Game.HEIGHT);
	}

	@Override
	public void exit() {
/*		entities.clear();*/
	}

	@Override
	public String getName() {
		return "Level_3";
	}

/*	public void addEntity(Entity entity) {
		entities.add(entity);
		
	}
	
	public void addTile(Tile tile) {
		tiles.add(tile);
	}

	
	public ArrayList<Tile> getTiles() {
		return tiles;
		
	}*/
	

}
