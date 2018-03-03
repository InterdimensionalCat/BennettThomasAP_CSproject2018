package game.entity;

public enum ActionState {

	JUMPING(true, false),
	FALLING(true, false),
	ON_TILE(false, false),
	MOVING_ON_TILE(false, true),
	ON_SLOPE(false, false),
	MOVING_ON_SLOPE(false, true),
	ON_MOVING_PLATFORM(false, false),
	MOVING_ON_MOVING_PLATFORM(false, true);
	
	private boolean isAirBorne;
	private boolean isMoving;
	
	private ActionState(boolean isAirborne, boolean isMoving) {
		this.isAirBorne = isAirborne;
		this.isMoving = isMoving;
	}
	
	public boolean isAirBorne() {
		return isAirBorne;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
}
