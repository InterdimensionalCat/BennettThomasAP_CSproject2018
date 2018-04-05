package game.entity;

import java.awt.geom.Line2D;

public enum AngleState {
	

	 RIGHT(1),
	 LEFT(3),
	 FLOOR(0),
	 CEILING(2);
	
	int id;
	public static AngleState[] index = {FLOOR, RIGHT, CEILING, LEFT};
	
	private AngleState(int id) {
		this.id = id;
	}
}
