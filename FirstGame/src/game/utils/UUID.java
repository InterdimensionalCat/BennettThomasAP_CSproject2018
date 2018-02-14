package game.utils;

public class UUID {
	
	private int ID;
	
	public UUID(int startingID) {
		this.ID = startingID;
	}
	
	public int next() {
		return ID++;
	}
	
	public int getCurrentID() {
		return ID;
	}
}
