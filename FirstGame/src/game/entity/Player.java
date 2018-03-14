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
	private double maxMotionX;
	private Animation idle;
	//private ActionState playerActionState;
	protected boolean turnRunRight;
	protected boolean turnRunLeft;
	private int deathCount = InitAnimations.playerDeath;
	private int score = InitAnimations.playerScore;
	public static volatile boolean playerDead;
	private int invincibleTime = 0;
	public boolean onMovingTile;
	public boolean hasCollision = true;
	public int tickerMove;

	public Player(double x, double y, TileMap tileMap) {
		super(new Texture(new Texture("PlayerIdleMap"), 1, 1, 64), x, y, tileMap, new Rectangle());


		this.AABB = new Rectangle((int)this.getCollisionX(),(int)this.getCollisionY(),this.getCollisionWidth(),this.getCollisionHeight());
		this.tileMap = tileMap;
		this.maxMotionX = 10.0;
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
/*				motionX -= 0.3;*/
				if (!(motionX > 0)) {
					tickerMove++;
					motionX += nextLogisticOne(motionX, false);
				} else {
					motionX -= 0.3;
					tickerMove = 0;
				}
				if(motionX < -maxMotionX*6) {
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
/*				motionX += 0.3;*/
				if(!(motionX < 0)) {
					tickerMove++;
					motionX += nextLogisticOne(motionX, true);
				} else {
					motionX += 0.3;
					tickerMove = 0;
				}
				if(motionX > maxMotionX*6) {
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
		
		if(KeyInput.wasPressed(KeyEvent.VK_O)) {
			deathCount--;
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_P)) {
			deathCount++;
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_G)) {
			invincibleTime += 180;
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_F)) {
			playerSpawnX = (int)x;
			playerSpawnY = (int)y;
		}
		
		if(KeyInput.wasReleased(KeyEvent.VK_A)||KeyInput.wasReleased(KeyEvent.VK_D)) {
			//motionX /= 4;
			moving = false;
			tickerMove = 0;
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
		
		animate();
		
		super.tick();
		System.out.println(motionX);
	}
	
	public void setDead() {
		if (invincibleTime <= 0) {
			System.out.println("You Died!");
			playerDead = true;
			Game.fxmanager.playSound("PlayerDead");
			if (++deathCount > 5) {
				System.err.println("Game Over!");
				Game.player.stop();
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
			this.idle = InitAnimations.animations.get("Player_idle");
		}
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
	
	protected void animate() {
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
	
	public double nextLogistic(double t, boolean positive) {
		double i;
		double t2 = t/3;
		double change = maxMotionX / (1 + 0.08*Math.pow(Math.E, -maxMotionX*0.01*(t2-1)));
		change = 0;
/*		if(positive) {
			//i = maxMotionX * (1.0000001) - 2*(1.0000001)*j;
			double A = maxMotionX;
			double c = 0.08;
			double k = 0.01;
			//double change = A / (1 + c*Math.pow(Math.E, -A*k*(t-1)));
			i = A / (1 + c*Math.pow(Math.E, -A*k*t));
			System.out.println(i - change);
			return i - change;
		} else {
			//i = -maxMotionX * (1.0000001) - 2*(1.0000001)*j;
			//i = (2.0)*(j)*((-maxMotionX-0.0000000001) - (j) + c);
			double A = -maxMotionX;
			double c = 0.08;
			double k = 0.01;
			//double change = -A / (1 + c*Math.pow(Math.E, A*k*(t-1)));
			i = A / (1 + c*Math.pow(Math.E, -A*k*t));
			System.out.println(-change + i);
			return -change + i;*/
			double A = maxMotionX;
			double c = 0.08;
			double k = 0.01;
			i = A / (1 + c*Math.pow(Math.E, -A*k*t2));
			if(positive) {
				System.out.println(i - change);
			    return i - change;
			} else {
				System.out.println(-(i - change));
			    return -(i - change);
			}


		//double i = -(2.0)*(j)*((maxMotionX-0.0000000001) - (j));
/*		System.out.println(-(2.0)*(j+1)*((maxMotionX-0.0000000001) - (j+1)));
		return -(2.0)*(j+1)*((maxMotionX-0.0000000001) - (j+1));*/
	}

	public double nextLogisticOne(double motion, boolean positive) {
		double N = Math.abs(motion);
		if(motion == 0) {
			N = 0.01;
		}
		double k = 15;
		double i = N*((k - N)/k);
		//System.out.println(i);
		if(positive) {
			return i / 6;
		} else {
			return -i / 6;
		}
		
	}
}
