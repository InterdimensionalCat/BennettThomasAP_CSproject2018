package game.world;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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
	protected double[] heightMask;
	protected double[] heightMask1;
	int x;
	int y;
	public Rectangle AABB;
	public double angle;
	
	public static final Rectangle constRect = new Rectangle(0,0,64,64);
	
	public static final Tile tile1 = new Tile(0xFF000000, new Texture(terrain, 3, 1 , 64 , 64), TileType.SOLID, createSolidArray() , createSolidArray() , Math.toRadians(0));
	public static final Tile tile2 = new Tile(0xFFFF0000, new Texture(terrain, 2, 1 , 64, 64), TileType.SLOPE_RIGHT_64_00, /*create64RightArray()*/ createSlopeRightArray(1,64), createSolidArray(), Math.toRadians(-45));
	
	public static final Tile tile25 = new Tile(0xFFFF0000, new Texture(terrain, 2, 1 , 64, 64), TileType.SLOPE_RIGHT_64_00, /*create64RightArray()*/ createSlopeLeftArray(1,64), createSlopeLeftArray(1,64) , Math.toRadians(45));
	
	
	public static final Tile slope1 = new Tile(-6, setSlopeImage(createSlopeLeftArray(0,16)), TileType.SLOPE_RIGHT_64_00, createSlopeRightArray(0,16), createSolidArray(), Math.toRadians( - 11.25));
	public static final Tile slope2 = new Tile(-6, setSlopeImage(createSlopeLeftArray(16,32)), TileType.SLOPE_RIGHT_64_00, createSlopeRightArray(16,32), createSolidArray(), Math.toRadians( - 11.25));
	public static final Tile slope3 = new Tile(-6, setSlopeImage(createSlopeLeftArray(32,48)), TileType.SLOPE_RIGHT_64_00, createSlopeRightArray(32,48), createSolidArray(), Math.toRadians( - 11.25));
	public static final Tile slope4 = new Tile(-6, setSlopeImage(createSlopeLeftArray(48,64)), TileType.SLOPE_RIGHT_64_00, createSlopeRightArray(48,64), createSolidArray(), Math.toRadians( - 11.25));
	
	public static final Tile slope5 = new Tile(-6, setSlopeImageLeft(createSlopeLeftArray(48,64)), TileType.SLOPE_RIGHT_64_00, createSlopeLeftArray(48,64), createSolidArray(), Math.toRadians(11.25));
	public static final Tile slope6 = new Tile(-6, setSlopeImageLeft(createSlopeLeftArray(32,48)), TileType.SLOPE_RIGHT_64_00, createSlopeLeftArray(32,48), createSolidArray(), Math.toRadians(11.25));
	public static final Tile slope7 = new Tile(-6, setSlopeImageLeft(createSlopeLeftArray(16,32)), TileType.SLOPE_RIGHT_64_00, createSlopeLeftArray(16,32), createSolidArray(), Math.toRadians(11.25));
	public static final Tile slope8 = new Tile(-6, setSlopeImageLeft(createSlopeLeftArray(1,16)), TileType.SLOPE_RIGHT_64_00, createSlopeLeftArray(1,16), createSolidArray(), Math.toRadians(11.25));
	
	public static final Tile air = new Tile(-2, new Texture(terrain, 1, 1 , 64, 64), TileType.AIR, createNoArray(), createNoArray(), Math.toRadians(0));
	public static final Tile tile3 = new Tile(0xFFFFFFFF, new Texture(terrain, 4, 1 , 64, 64), TileType.AIR, createNoArray(), createNoArray(), Math.toRadians(0));
	public static final Tile tile4 = new Tile(0xFF00FFFF, new Texture(terrain, 1, 2 , 64, 64), TileType.SOLID, createSolidArray(), createSolidArray() , Math.toRadians(0));
	public static final Tile tile5 = new Tile(0xFF00FF00, new Texture(terrain, 2, 2 , 64, 64), TileType.SOLID, createSolidArray(), createSolidArray() , Math.toRadians(0));
	public static final Tile tile6 = new Tile(0xFFF0F0F0, new Texture(terrain, 3, 2 , 64, 64), TileType.SOLID, createSolidArray(), createSolidArray() , Math.toRadians(0));
	public static final Tile tile7 = new Tile(0xFF0F0F0F, new Texture(terrain, 4, 2 , 64, 64), TileType.SOLID, createSolidArray(), createSolidArray() , Math.toRadians(0));
	public static final Tile tile8 = new Tile(0xFF111111, new Texture(terrain, 1, 3 , 64, 64), TileType.SOLID, createSolidArray(), createSolidArray() , Math.toRadians(0));
	public static final Tile tile9 = new Tile(0xFF222222, new Texture(terrain, 2, 3 , 64, 64), TileType.SOLID, createSolidArray(), createSolidArray() , Math.toRadians(0));
	
	private Tile(int id, Texture sprite, TileType type, double[] heightMask, double[] heightMask1, double angle) { //creates tile type constants
		this.id = id;
		this.sprite = sprite;
		tileMap.put(id, this);
		this.type = type;
		
		if (this.type.isNotAir()) {
			solid = true;
		} else {
			solid = false;
		}
		
		this.heightMask = heightMask;
		for(int i = 0; i < this.heightMask.length; i++) {
			if(this.heightMask[i] == 0) {
				this.heightMask[i] = 1;
			}
		}
		
		this.x = 0;
		this.y = 0;
		this.AABB = constRect;
		this.angle = angle;
		//System.out.println(angle);
		
	}
	
	public Tile(Tile t, int x, int y) {
		this.id = t.id;
		this.sprite = t.sprite;
		solid = t.solid;
		this.type = t.type;
		this.heightMask = t.heightMask;
		this.x = x;
		this.y = y;
		this.AABB = new Rectangle(x,y,64,64);
		this.angle = t.angle;
		this.heightMask1 = heightMask1;
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
	
	public static double[] createSolidArray() {
		double[] arr = new double[64];
		for(int i = 0; i < 64; i++) {
			arr[i] = 64;
		}
		return arr;
	}
	
	public static double[] createNoArray() {
		double[] arr = new double[64];
		for(int i = 0; i < 64; i++) {
			arr[i] = 0;
		}
		return arr;
	}
	
	public static double[] create64RightArray() {
		double[] arr = new double[64];
		for(int i = 0; i < 64; i++) {
			arr[i] = i+1;
		}
		
		//arr[63] = 60;
		
		return arr;
	}
	
	public static double[] createSlopeRightArray(int lower, int upper) {
		double[] arr = new double[64];
		int spacing = 64 / (upper-lower);
		int c = 0;
		for(int i = 0; i < 64; i++) {
			for(int j = 0; j < spacing; j++) {
				if(i+j >= 64) {
					arr[63] = lower + c;
				} else {
					arr[i+j] = lower + c;
				}
			}
			i+= spacing - 1;
			c++;
		}
		return arr;
	}
	
	public static double[] createSlopeLeftArray(int lower, int upper) {
		double[] arr = createSlopeRightArray(lower, upper);
		double[] ar1 = arr.clone();
		for(int i = 0; i < arr.length; i++) {
			ar1[i] = arr[arr.length - i - 1];
		}
		return ar1;
	}
	
	public static Texture setSlopeImage(double[] heightMask) {
		BufferedImage b = tile1.sprite.getImage();
		int[] pixels = b.getRGB(0, 0, b.getWidth(), b.getHeight(), null, 0, b.getWidth());
		BufferedImage b1 = new BufferedImage(b.getWidth(), b.getHeight(), b.getType());
		for(int i = 0; i < b.getWidth(); i++ ) {
			for(int j = b.getHeight() - (int)heightMask[heightMask.length - i - 1]; j < b.getHeight() ; j++) {
				b1.setRGB(i, j, pixels[ (i) + (j)*(b.getWidth())]);
			}
		}
		Texture t = new Texture(terrain, 3, 1 , 64 , 64);
		t.setBufferedImage(b1);
		return t;
	}
	
	public static Texture setSlopeImageLeft(double[] heightMask) {
		double[] hm1 = new double[heightMask.length];
		for(int i = 0; i < heightMask.length; i ++) {
			hm1[hm1.length - 1 - i] = heightMask[i];
		}
		BufferedImage b = tile1.sprite.getImage();
		int[] pixels = b.getRGB(0, 0, b.getWidth(), b.getHeight(), null, 0, b.getWidth());
		BufferedImage b1 = new BufferedImage(b.getWidth(), b.getHeight(), b.getType());
		for(int i = 0; i < b.getWidth(); i++ ) {
			for(int j = b.getHeight() - (int)hm1[hm1.length - i - 1]; j < b.getHeight() ; j++) {
				b1.setRGB(i, j, pixels[ (i) + (j)*(b.getWidth())]);
			}
		}
		
		
		Texture t = new Texture(terrain, 3, 1 , 64 , 64);
		t.setBufferedImage(b1);
		return t;
	}
	
	public double getAngleFromHeightMask(double x1, double x2) {
		return Math.atan(Math.min(x1, x2) / Math.max(x1, x2));
	}
	
}
