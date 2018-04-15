package game.world;

import java.util.Arrays;

import game.render.textures.Texture;

/** 
 *4x4 tile maps, so 256x256px. (Technically it can be any size, but 4x4 is all I am using)
 *Done because many spritemaps for tiles are just 4x4's that are the copied into the world so this makes it easier
 *Square spritemaps only.
 */


public class Chunk {
	
	
	public Tile[] map;
	public Texture texture;
	public int size;
	
	public Chunk(Texture t, int size, double[] angles, int id) {
		this.texture = t;
		this.size = size;
		map = new Tile[size*size];
		for(int i = 0; i < size; i++) { // y value
			for(int j = 0; j < size; j++) { // x value
				map[(j + i*size)] = new Tile(id, new Texture(texture, j + 1, i + 1 , 64, 64), TileType.TEST, new double[] {} , new double[] {} , Math.toRadians(angles[j + i*size]));
			}
		}
		
		for(int i = 0; i < map.length; i ++) {
			if(Arrays.equals(map[i].heightMaskCeil, map[i].heightMaskFloor)&&Arrays.equals(map[i].heightMaskCeil, map[i].heightMaskRight)&&Arrays.equals(map[i].heightMaskCeil, map[i].heightMaskLeft)&&Arrays.equals(Tile.air.heightMaskFloor, map[i].heightMaskFloor)) {
				map[i] = Tile.air;
			}
/*			if(i == 4) {
				map[i].type = TileType.SLOPE_LEFT_64_00;
			}*/
		}
		
	}
	
	public Chunk(Texture t, int size, double[] angles, int id, TileType type) {
		this(t, size,angles,id);
		setChunkType(type);

	}
	
	public void setChunkType(TileType t) {
		for(int i = 0; i < map.length; i++) {
			map[i].type = t;
			if(t == TileType.AIR) {
				map[i].solid = false;
			}
		}
	}
	
	public void setTileType(TileType t, int x, int y) {
		map[(x + y*size)].type = t;
	}
}
