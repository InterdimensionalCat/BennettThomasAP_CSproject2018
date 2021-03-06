/*package game.entity;

import game.render.textures.Animation;
import game.utils.init.InitAnimations;

public enum ActionState {

	JUMPING(true, false, true, InitAnimations.animations.get("Player_jump")),
	MOVING_JUMPING(true, true, true, InitAnimations.animations.get("Player_jump")),
	FALLING(true, false, true, InitAnimations.animations.get("Player_jump")),
	MOVING_FALLING(true, true, true, InitAnimations.animations.get("Player_jump")),
	ON_TILE(false, false, true, InitAnimations.animations.get("Player_idle")),
	MOVING_ON_TILE(false, true, true, InitAnimations.animations.get("Player_run")),
	ON_SLOPE(false, false, false, InitAnimations.animations.get("Player_idle")),
	MOVING_ON_SLOPE(false, true, false, InitAnimations.animations.get("Player_run")),
	ON_MOVING_PLATFORM(false, false, true, InitAnimations.animations.get("Player_idle")),
	MOVING_ON_MOVING_PLATFORM(false, true, true, InitAnimations.animations.get("Player_run")),
	TURN_RUN_LEFT(false, true, true, InitAnimations.animations.get("Player_turnRun")),
	TURN_RUN_RIGHT(false, true, true, InitAnimations.animations.get("Player_turnRun")),
	TURN_RUN_ON_SLOPE_LEFT(false, true, false, InitAnimations.animations.get("Player_turnRun")),
	TURN_RUN_ON_SLOPE_RIGHT(false, true, false, InitAnimations.animations.get("Player_turnRun")),
	TURN_RUN_ON_MOVING_PLATFORM_LEFT(false, true, false, InitAnimations.animations.get("Player_turnRun")),
	TURN_RUN_ON_MOVING_PLATFORM_RIGHT(false, true, false, InitAnimations.animations.get("Player_turnRun")),
	
	MOB_MOVING(false, true, true, InitAnimations.animations.get("Boop_walk")),
	MOB_STATIONARY(false, false, true, InitAnimations.animations.get("Boop_walk")),
	MOB_AIR_STATIONARY(true, false, true, InitAnimations.animations.get("Boop_walk")),
	MOB_MOVING_ON_SLOPE(false, true, false, InitAnimations.animations.get("Boop_walk")),
	MOB_MOVING_ON_MOVING_PLATFORM(false, true, true, InitAnimations.animations.get("Boop_walk")),
	MOB_AIR_MOVING(true, true, true, InitAnimations.animations.get("Boop_walk")),
	NO_GRAVITY(true, true, false, InitAnimations.animations.get("Boop_walk"));
	
	
	private boolean isAirBorne;
	private boolean isMoving;
	private boolean hasCollision;
	private Animation stateAnimation;
	
	private ActionState(boolean isAirborne, boolean isMoving, boolean hasCollision, Animation animation) {
		this.isAirBorne = isAirborne;
		this.isMoving = isMoving;
		this.hasCollision = hasCollision;
		this.stateAnimation = animation;
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
	
	public boolean isFalling() {
		return this == FALLING || this == MOVING_FALLING;
	}
	
	public boolean isIdle() {
		return this == ON_TILE || this == ON_SLOPE || this == ON_MOVING_PLATFORM;
	}
	
	public boolean isTurnRunLeft() {
		return this == TURN_RUN_LEFT || this == TURN_RUN_ON_SLOPE_LEFT || this == TURN_RUN_ON_MOVING_PLATFORM_LEFT;
	}
	
	public boolean isTurnRunRight() {
		return this == TURN_RUN_RIGHT || this == TURN_RUN_ON_SLOPE_RIGHT || this == TURN_RUN_ON_MOVING_PLATFORM_RIGHT;
	}
	
	
	public ActionState toSlope() {
		
		if(this == TURN_RUN_LEFT) {
			return TURN_RUN_ON_SLOPE_LEFT;
		}
		
		if(this == TURN_RUN_RIGHT) {
			return TURN_RUN_ON_SLOPE_RIGHT;
		}
		if(this == MOB_MOVING) {
			return MOB_MOVING_ON_SLOPE;
		}
		
		if(this.isMoving) {
			return MOVING_ON_SLOPE;
		} else {
			return ON_SLOPE;
		}
	}
	
	public ActionState toPlatform() {
		
		if(this.stateAnimation == InitAnimations.animations.get("Boop_walk")) {
			if(this.isMoving) {
				return MOB_MOVING_ON_MOVING_PLATFORM;
			} else {
				return MOB_STATIONARY;
			}
		}
		
		if(this == TURN_RUN_LEFT) {
			return TURN_RUN_ON_MOVING_PLATFORM_LEFT;
		}
		
		if(this == TURN_RUN_RIGHT) {
			return TURN_RUN_ON_MOVING_PLATFORM_RIGHT;
		}
		
		if(this.isMoving) {
			return MOVING_ON_MOVING_PLATFORM;
		} else {
			return ON_MOVING_PLATFORM;
		}
	}
	
	public ActionState toMoving() {
		if(this == ON_TILE) {
			return MOVING_ON_TILE;
		}
		if(this == ON_SLOPE) {
			return MOVING_ON_SLOPE;
		}
		if(this == ON_MOVING_PLATFORM) {
			return MOVING_ON_MOVING_PLATFORM;
		}
		if(this == JUMPING) {
			return MOVING_JUMPING;
		}
		if(this == FALLING) {
			return MOVING_FALLING;
		}
		System.err.println("Action State cannot be converted to moving");
		return this;
	}
	
	public ActionState toStationary() {
		if(this.stateAnimation == InitAnimations.animations.get("Boop_walk")) {
			if(this.isAirBorne) {
				return MOB_AIR_STATIONARY;
			} else {
				return MOB_STATIONARY;
			}
		}
		if(this.isAirBorne) {
			return FALLING;
		} else {
			if(this == MOVING_ON_MOVING_PLATFORM || this == TURN_RUN_ON_MOVING_PLATFORM_LEFT || this == TURN_RUN_ON_MOVING_PLATFORM_RIGHT) {
				return ON_MOVING_PLATFORM;
			}
			if(this == MOVING_ON_SLOPE|| this == TURN_RUN_ON_SLOPE_LEFT || this == TURN_RUN_ON_SLOPE_RIGHT) {
				return ON_SLOPE;
			}
			if(this == MOVING_ON_TILE|| this == TURN_RUN_LEFT || this == TURN_RUN_RIGHT) {
				return ON_TILE;
			}
		}
		System.err.println("Could not be made stationary");
		return this;
	}
	
	public ActionState toAirBorne() {
		if(this.isMoving) {
			return MOVING_FALLING;
		} else {
			return FALLING;
		}
	}
	
	public boolean isOnPlatform() {
		return this == ON_MOVING_PLATFORM || this == MOVING_ON_MOVING_PLATFORM || this == MOB_MOVING_ON_MOVING_PLATFORM || this == TURN_RUN_ON_MOVING_PLATFORM_LEFT ||  this == TURN_RUN_ON_MOVING_PLATFORM_RIGHT;
	}
	
	public boolean isOnSlope() {
		return this == ON_SLOPE || this == MOVING_ON_SLOPE || this == MOB_MOVING_ON_SLOPE || this == TURN_RUN_ON_SLOPE_LEFT || this == TURN_RUN_ON_SLOPE_RIGHT;
	}
	
	public Animation getStateAnimation() {
		return stateAnimation;
	}
}
*/