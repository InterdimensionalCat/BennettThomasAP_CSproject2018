package game.entity;

public enum ActionState {

	JUMPING(true, false, true),
	FALLING(true, false, true),
	ON_TILE(false, false, true),
	MOVING_ON_TILE(false, true, true),
	ON_SLOPE(false, false, false),
	MOVING_ON_SLOPE(false, true, false),
	ON_MOVING_PLATFORM(false, false, true),
	MOVING_ON_MOVING_PLATFORM(false, true, true);
	
	private boolean isAirBorne;
	private boolean isMoving;
	private boolean hasCollision;
	
	private ActionState(boolean isAirborne, boolean isMoving, boolean hasCollision) {
		this.isAirBorne = isAirborne;
		this.isMoving = isMoving;
		this.hasCollision = hasCollision;
	}
	
	public boolean isAirBorne() {
		return isAirBorne;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public boolean hasCollision() {
		return hasCollision;
	}
}
