package game.world;

public enum TileType {

	SOLID(64, 64),
	SLOPE_RIGHT_64_00(64, 0),
	SLOPE_LEFT_64_00(0, 64),
	AIR(0,0),
	TEST(0,0);
	
	
	
	private TileType(int y1, int y2) {
		
	}
	
	public static boolean isCubeType(TileType t) {
		return t == SOLID;
	}
	
	public static boolean isSlopeRightType(TileType t) {
		return t == SLOPE_RIGHT_64_00;
	}
	
	public static boolean isSlopeLeftType(TileType t) {
		return t == SLOPE_LEFT_64_00;
	}
	
	public static boolean isSlope(TileType t) {
		return isSlopeRightType(t) || isSlopeLeftType(t);
	}
	
	public boolean isNotAir() {
		return this != AIR;
	}
}
