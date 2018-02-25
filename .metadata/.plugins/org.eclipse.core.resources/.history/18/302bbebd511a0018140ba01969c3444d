package game.world;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import game.render.textures.Texture;
import game.utils.init.InitTextures;

public class Tile {
	

	private static final Texture terrain = new Texture("SpriteMap1");
	private static final Map<Integer, Tile> tileMap = new HashMap<Integer, Tile>();
	public Texture sprite;
	protected boolean solid = true;
	protected int id;
	protected TileType type;
	
	public static final Tile tile1 = new Tile(0xFF000000, new Texture(terrain, 3, 1 , 64 , 64), TileType.SOLID);
	public static final Tile tile2 = new Tile(0xFFFF0000, new Texture(terrain, 2, 1 , 64, 64), TileType.SOLID /*TileType.SLOPE_RIGHT_64_00*/);
	public static final Tile tile3 = new Tile(0xFFFFFFFF, new Texture(terrain, 4, 1 , 64, 64), TileType.SOLID);
	public static final Tile tile4 = new Tile(0xFF0000FF, new Texture(terrain, 1, 2 , 64, 64), TileType.SOLID);
	public static final Tile tile5 = new Tile(0xFF00FF00, new Texture(terrain, 2, 2 , 64, 64), TileType.SOLID);
	
	private Tile(int id, Texture sprite, TileType type) {
		this.id = id;
		this.sprite = sprite;
		tileMap.put(id, this);
		solid = true;
		this.type = type;
	}
	

	
	public void render(Graphics2D g, int x, int y) {
		sprite.render(g, x, y);
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public static Tile getFromID(int id) {
		return tileMap.get(id);
	}
}
