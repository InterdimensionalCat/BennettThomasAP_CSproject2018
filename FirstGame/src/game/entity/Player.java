package game.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import game.Game;
import game.input.KeyInput;
import game.render.textures.Animation;
import game.render.textures.Texture;
import game.utils.Fonts;
import game.utils.init.InitAnimations;
import game.utils.init.InitAudio;
import game.utils.init.InitLevels;
import game.utils.init.InitTextures;
import game.world.TileMap;

public class Player extends Mob {
	
	private int playerSpawnX;
	private int playerSpawnY;
	private TileMap tileMap;
	private double maxMotionX;
	private Animation idle;
	private ActionState playerActionState;
	protected boolean turnRunRight;
	protected boolean turnRunLeft;
	private int deathCount = InitAnimations.playerDeath;
	private int score = InitAnimations.playerScore;
	public static volatile boolean playerDead;
	private int invincibleTime = 0;

	public Player(double x, double y, TileMap tileMap) {
		super(new Texture(new Texture("PlayerIdleMap"), 1, 1, 64), x, y, tileMap, new Rectangle());


		this.AABB = new Rectangle((int)this.getCollisionX(),(int)this.getCollisionY(),this.getCollisionWidth(),this.getCollisionHeight());
		this.tileMap = tileMap;
		this.maxMotionX = 6.0;
		this.idle = InitAnimations.animations.get("Player_idle");
		//playerActionState = ActionState.FALLING;
	}
	
	@Override
	public void render(Graphics2D g, int offsetX, int offsetY) {
		idle.render(g, x + offsetX, y + offsetY);
		Fonts.drawString(g, new Font("Arial", Font.BOLD, 30) , Color.BLACK, "Deaths: " + deathCount, 10, 30);
		Fonts.drawString(g, new Font("Arial", Font.BOLD, 30) , Color.BLACK, "Score: " + score, 10, 60);
	}

	@Override
	public void tick() {

		turnRunRight = false;
		turnRunLeft = false;
		if (invincibleTime > 0) {
			invincibleTime--;
		}
		InitAnimations.playerDeath = deathCount;
		InitAnimations.playerScore = score;
		
		if(KeyInput.isDown(KeyEvent.VK_A)) {
			moving = true;
			if(isAirBorne) {
				motionX -= 6.0;
				if(motionX < -maxMotionX) {
					motionX = -maxMotionX;
				}
			} else {
				motionX -= 0.3;
				if(motionX < -maxMotionX) {
					motionX = -maxMotionX;
				}
			}
			
			if(motionX > 0) {
				turnRunRight = true;
			}
			
		}
		
		
		
		if(KeyInput.isDown(KeyEvent.VK_D)) {
			moving = true;
			if(isAirBorne) {
				motionX += 6.0;
				if(motionX > maxMotionX) {
					motionX = maxMotionX;
				}
			} else {
				motionX += 0.3;
				if(motionX > maxMotionX) {
					motionX = maxMotionX;
				}
			}
			if(motionX < 0) {
				turnRunLeft = true;
			}
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_SPACE)) {
			jump(15.0);
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_1)) {
			Game.pauseTime+= 100;
			Game.level.nextLevel();
		}
		
		if(KeyInput.wasReleased(KeyEvent.VK_A)||KeyInput.wasReleased(KeyEvent.VK_D)) {
			//motionX /= 4;
			moving = false;
		}
		
		if(KeyInput.wasReleased(KeyEvent.VK_SPACE) && motionY < 0) {
			motionY /= 2.0; //1.5;
		}
		
		if(!moving) {
			//idle.run();
			if(motionX < 1 && motionX > -1) {
				motionX = 0;
			} else {
				if(motionX > 0) {
					//motionX -= 0.6;
					motionX /= 1.4;
				}
				if(motionX < 0) {
					//motionX += 0.6;
					motionX /= 1.4;
				}
			}
			if(isAirBorne) {
				motionX = 0;
			}
		}
		
		if(!moving && !isAirBorne) {
			this.idle = InitAnimations.animations.get("Player_idle");
			if (InitAnimations.animations.get("Player_run").getFlip() == true)  {
				this.idle.setFlip(true);
			} else {
				this.idle.setFlip(false);
			}
			InitAnimations.animations.get("Player_idle").run();
		} else {
				if (isAirBorne) {
					this.idle = InitAnimations.animations.get("Player_jump");
					InitAnimations.animations.get("Player_jump").run();
				} else {
					if(moving) {
						if (turnRunLeft) {
							this.idle = InitAnimations.animations.get("Player_turnRun");
							this.idle.setFlip(true);
							InitAnimations.animations.get("Player_turnRun").setFlip(true);
							InitAnimations.animations.get("Player_turnRun").run();
						} else {
							if (turnRunRight) {
								this.idle = InitAnimations.animations.get("Player_turnRun");
								this.idle.setFlip(false);
								InitAnimations.animations.get("Player_turnRun").setFlip(false);
								InitAnimations.animations.get("Player_turnRun").run();
							} else {
								this.idle = InitAnimations.animations.get("Player_run");
								if(this.motionX < 0) {
									this.idle.setFlip(true);
									InitAnimations.animations.get("Player_run").setFlip(true);
									}
								if(this.motionX > 0) {
									this.idle.setFlip(false);
									InitAnimations.animations.get("Player_run").setFlip(false);
								}
								//this.idle.setSpeed(30 - (int)Math.abs(2*motionX)); //oof this isnt working as intended
								InitAnimations.animations.get("Player_run").run();
							}
						}
					}
				}
				
		}
		
		
		super.tick();
	}
	
	protected void jump(double velocityY) {
		if(!isAirBorne) {
			if(!InitAudio.musicFiles.get("PlayerJump1").isPlaying()) {
				Game.fxmanager.playSound("PlayerJump1");
			} else {
				Game.fxmanager.playSound("PlayerJump2");
			}
		}
		super.jump(velocityY);
	}
	
	public int getCollisionWidth() {
		return texture.getWidth() - 24;
	}
	
	public double getCollisionX() {
		return x + 5;
	}
	
	public double adjustXforCollision(double x) {
		return x + 5;
	}
	
	public double adjustYforCollision(double y) {
		return y + 10;
	}
	
	public double AABBtoX(double x) {
		return x - 5;
	}
	
	public double AABBtoY(double y) {
		return y - 10;
	}
	
	
	public int getCollisionHeight() {
		return texture.getHeight() - 10;
	}
	
	public double getCollisionY() {
		return y + 10;
	}


	public Rectangle getAABB() {
		return AABB;
	}
	
	public void setDead() {
		if (invincibleTime <= 0) {
			System.out.println("You Died!");
			playerDead = true;
			Game.fxmanager.playSound("PlayerDead");
			if (++deathCount > 5) {
				System.err.println("Game Over!");
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			Game.pauseTime += 20;
			x = playerSpawnX;
			y = playerSpawnY;
			motionX = 0;
			motionY = 0;
			invincibleTime = 180;
		}
	}
	
	public int getPlayerSpawnX() {
		return playerSpawnX;
	}
	
	public int getPlayerSpawnY() {
		return playerSpawnY;
	}
	
	public void setPlayerSpawnX(int x) {
		playerSpawnX = x;
	}
	
	public void setPlayerSpawnY(int y) {
		playerSpawnY = y;
	}
	
	public void score() {
		score++;
	}

}
