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
	protected boolean turnRunRight;
	protected boolean turnRunLeft;
	private int deathCount = InitAnimations.playerDeath;
	private int score = InitAnimations.playerScore;
	public static volatile boolean playerDead;
	private int invincibleTime = 0;
	public boolean onMovingTile;
	public boolean hasCollision = true;
	public int tickerMove;
	boolean conflict = false;
	boolean leftPriority;
	boolean rightPriority;
	
	private final double acc =  0.046875;
	private final double dec = 0.5;
	private final double frc = 0.06875;
	private double top = 6.0;
	
	

	public Player(double x, double y, TileMap tileMap) {
		super(new Texture(new Texture("PlayerIdleMap"), 1, 1, 64), x, y, tileMap, new Rectangle());


		this.AABB = new Rectangle((int)x,(int)y,64,64);
		this.tileMap = tileMap;
		this.maxMotionX = 15.0;
		this.idle = InitAnimations.animations.get("Player_idle");
		//this.state = ActionState.FALLING;
		falling = false;
	}
	
	@Override
	public void render(Graphics2D g, int offsetX, int offsetY) {
		idle.render(g, x + offsetX, y + offsetY);
		Fonts.drawString(g, new Font("Arial", Font.BOLD, 30) , Color.BLACK, "Deaths: " + deathCount, 10, 30);
		Fonts.drawString(g, new Font("Arial", Font.BOLD, 30) , Color.BLACK, "Score: " + score, 10, 60);
		if(falling) {
			Fonts.drawString(g, new Font("Arial", Font.BOLD, 30) , Color.BLACK, "Speed: " + (int)xsp, 10, 90);
		} else {
			Fonts.drawString(g, new Font("Arial", Font.BOLD, 30) , Color.BLACK, "Speed: " + (int)gsp, 10, 90);
		}

		
		if (Game.debug) {
			g.setColor(Color.BLACK);
			g.drawLine((int) centerLine.getX1() + offsetX, (int) centerLine.getY1() + offsetY,
					(int) centerLine.getX2() + offsetX, (int) centerLine.getY2() + offsetY);
			g.drawLine((int) floorCheck1.getX1() + offsetX, (int) floorCheck1.getY1() + offsetY,
					(int) floorCheck1.getX2() + offsetX, (int) floorCheck1.getY2() + offsetY);
			g.drawLine((int) floorCheck2.getX1() + offsetX, (int) floorCheck2.getY1() + offsetY,
					(int) floorCheck2.getX2() + offsetX, (int) floorCheck2.getY2() + offsetY);
			g.drawLine((int) ceilCheck1.getX1() + offsetX, (int) ceilCheck1.getY1() + offsetY,
					(int) ceilCheck1.getX2() + offsetX, (int) ceilCheck1.getY2() + offsetY);
			/*g.drawLine((int) ceilCheck2.getX1() + offsetX, (int) ceilCheck2.getY1() + offsetY,
					(int) ceilCheck2.getX2() + offsetX, (int) ceilCheck2.getY2() + offsetY);*/
			g.drawRect((int)x + offsetX, (int)y + offsetY, 64, 64);
		}
	}

	@Override
	public void tick() {

		
		if(!rolling) {
			top = 6;
		} else {
			if(Math.abs(gsp) < 0.5) {
				rolling = false;
				top = 6;
			}
		}

		
		//turnRunRight = false;
		//turnRunLeft = false;
		if (invincibleTime > 0) {
			invincibleTime--;
		}
		InitAnimations.playerDeath = deathCount;
		InitAnimations.playerScore = score;
		
		conflict = false;
		leftPriority = false;
		rightPriority = false;
		
/*		if(KeyInput.isDown(KeyEvent.VK_A)&&KeyInput.isDown(KeyEvent.VK_D)) {
			if(gsp > 0) {
				rightPriority = true;
			} else {
				if(gsp < 0) {
					leftPriority = true;
				} else {
					leftPriority = true;
					rightPriority = true;
			    }
			}
		}*/
		
/*		if(KeyInput.isDown(KeyEvent.VK_A)&&!rightPriority) {
		    //this.state = this.state.toMoving();
		    moving = true;
			if(this.isAirBorne()) {
				if (!(gsp > 0)) {
					//motionX -= 6.0;
					gsp += -nextRestricted(-gsp) *//** 6*//*;
					if (gsp < -maxMotionX) {
						gsp = -maxMotionX;
					}
					if (gsp > -2) {
						gsp *= 2;
					}
					
					if(gsp < -9.5) {
						gsp = -10;
					}
					
				} else {
					gsp -= 2.0;
					
					if(gsp < -maxMotionX + 0.5) {
						gsp = -maxMotionX;
					}
					
				}
			} else {
				motionX -= 0.3;
				if (!(gsp > 0)) {
					tickerMove++;
					gsp += -nextRestricted(-gsp);
					if(gsp > -2) {
						gsp *= 2;
					}
					
					if(gsp < -maxMotionX + 0.5) {
						gsp = -maxMotionX;
					}
					
				} else {
					gsp -= 0.3;
					tickerMove = 0;
				}
				if(gsp < -maxMotionX*6) {
					gsp = -maxMotionX;
				}
			}
			
			if(gsp > 0) {
				//this.state = ActionState.TURN_RUN_RIGHT;
				turnRunRight = true;
			}
			
		}*/
		
		
		
/*		if(KeyInput.isDown(KeyEvent.VK_D)&&!leftPriority) {
			//this.state = this.state.toMoving();
			moving = true;
			if(this.isAirBorne()) {
				if (!(gsp < 0)) {
					//motionX += 6.0;
					gsp += nextRestricted(gsp) *//** 6*//*;
					if (gsp > maxMotionX) {
						gsp = maxMotionX;
					}
					if (gsp < 2) {
						gsp *= 2;
					}
					if (gsp > maxMotionX - 0.5) {
						gsp = maxMotionX;
					} 
				} else {
					gsp += 2.0;
					
					if (gsp > maxMotionX - 0.5) {
						gsp = maxMotionX;
					} 
					
				}
				
			} else {
				motionX += 0.3;
				if(!(gsp < 0)) {
					tickerMove++;
					gsp += nextRestricted(gsp);
					if(gsp < 2) {
						gsp *= 2;
					}
				} else {
					gsp += 0.3;
					tickerMove = 0;
				}
				if(gsp > maxMotionX*6) {
					gsp = maxMotionX;
				}
			}
			if(gsp < 0) {
				turnRunLeft = true;
			}
		}*/
		
		if(KeyInput.wasPressed(KeyEvent.VK_SPACE)) {
			jump(jmp);
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_S)) {
			if (Math.abs(gsp)  >  0.53125) {
				rolling = true;
			}
			top = 16;
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
		
		if(KeyInput.wasPressed(KeyEvent.VK_Q)) {
			//gsp = -maxMotionX;
			setSpeed(-maxMotionX);
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_E)) {
			//gsp = maxMotionX;
			setSpeed(maxMotionX);
		}
		
		if(KeyInput.wasReleased(KeyEvent.VK_A)||KeyInput.wasReleased(KeyEvent.VK_D)) {
			//motionX /= 4;
			//this.moving = false;
			//tickerMove = 0;
		}
		
		if(KeyInput.isDown(KeyEvent.VK_A)&&KeyInput.isDown(KeyEvent.VK_D)) {
			//motionX /= 4;
			//moving = false;
			//motionX /= 4;
		}
		
		if(KeyInput.wasReleased(KeyEvent.VK_SPACE)) {
			//motionY /= 2.0; //1.5;
			//jumpTimer/=2;
		}
		
		if(!KeyInput.isDown(KeyEvent.VK_SPACE)&& ysp < -4) {
			//ysp = -4;
		}
		
		/*if(!moving) {
			//idle.run();
			if(gsp < 1 && gsp > -1) {
				gsp = 0;
			} else {
				if(gsp > 0) {
					//motionX -= 0.6;
					gsp /= 1.4;
				}
				if(gsp < 0) {
					//motionX += 0.6;
					gsp /= 1.4;
				}
			}
			if(isAirBorne) {
				//motionX = 0;
			}
		}*/
		
		animate();
		
		super.tick();
		//System.out.println(motionX);
		
		this.AABB.setLocation((int)x, (int)y);
	}
	
	public void move() {
		
		if(falling) {
			rolling = false;
			//gsp = 0;
		}
		
		if(!(KeyInput.isDown(KeyEvent.VK_A)||KeyInput.isDown(KeyEvent.VK_D))||rolling) {
			
			moving = false;
			
			if(Math.abs(gsp) < frc) {
				gsp = 0;
				//setSpeed(0);
				turnRunLeft = false;
				turnRunRight = false;
			}
			
	/*		if(falling) {
				if(Math.abs(xsp) < frc) {
					setSpeed(0);
					turnRunLeft = false;
					turnRunRight = false;
				}
			}*/
			
			if(getSpeed() > 0) {
				//gsp -= frc;
				
				addSpeed(-frc);
				
				
				if(getSpeed() > 4.5 && angleState == AngleState.FLOOR) {
					turnRunRight = true;
				}
			}
			
			if(getSpeed() < 0) {
				//gsp += frc;
				
				addSpeed(frc);
				
				if(getSpeed() < -4.5 && angleState == AngleState.FLOOR) {
					turnRunLeft = true;
				}
				
			}
			
		} else {
			if(KeyInput.isDown(KeyEvent.VK_A)&&!rolling) {
				moving = true;
				if(getSpeed() > 0) {
					//gsp -= dec;
					addSpeed(-dec);
					if(getSpeed() > 4.5 && angleState == AngleState.FLOOR) {
						turnRunRight = true;
					}
				} else {
					//gsp -= acc;
					
					if (!(getSpeed() <= -top)) {
						addSpeed(-acc);
					}
					turnRunLeft = false;
					turnRunRight = false;
				}
			}
			
			if(KeyInput.isDown(KeyEvent.VK_D)&&!rolling) {
				moving = true;
				if(getSpeed() < 0) {
					//gsp += dec;
					
					addSpeed(dec);
					
					if(getSpeed() < -4.5 && angleState == AngleState.FLOOR) {
						turnRunLeft = true;
					}
				} else {
					//gsp += acc;
					
					if (!(getSpeed() >= top)) {
						addSpeed(acc);
					}
					
					turnRunLeft = false;
					turnRunRight = false;
				}
			}
		}

/*		if(getSpeed() > top) {
			//gsp = top;
			setSpeed(top);
		}
		
		if(getSpeed() < -top) {
			//gsp = -top;
			setSpeed(-top);
		}*/
		
		if(!falling) {
			
			if(rolling) {
				if(Math.signum(Math.sin(angle)) != Math.signum(gsp)) {
					gsp +=  0.078125*Math.sin(angle);
				} else {
					gsp +=  0.3125*Math.sin(angle);
				}
			} else {
				gsp += slp*Math.sin(angle);
			}
		}
		
		super.move();
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
		if(!falling) {
			if(!InitAudio.musicFiles.get("PlayerJump1").isPlaying()) {
				Game.fxmanager.playSound("PlayerJump1");
			} else {
				Game.fxmanager.playSound("PlayerJump2");
			}
			
			falling = true;
			super.jump(velocityY);
		}
	}
	
	protected void animate() {
		
		if(rolling) {
			this.idle = InitAnimations.animations.get("Player_jump");
			InitAnimations.animations.get("Player_jump").run();
			return;
		}
		
		if(!moving && (!isAirBorne&&!falling)) {
			this.idle = InitAnimations.animations.get("Player_idle");
			if (InitAnimations.animations.get("Player_run").getFlip() == true)  {
				this.idle.setFlip(true);
			} else {
				this.idle.setFlip(false);
			}
			InitAnimations.animations.get("Player_idle").run();
		} else {
				if (isAirBorne||falling) {
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
								if(this.gsp < 0) {
									this.idle.setFlip(true);
									InitAnimations.animations.get("Player_run").setFlip(true);
									}
								if(this.gsp > 0) {
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
	
	/*protected void animate() {
		this.idle = this.state.getStateAnimation();
		if (this.state.isIdle()) {
			if (InitAnimations.animations.get("Player_run").getFlip() == true) {
				this.idle.setFlip(true);
				this.idle.run();
				return;
			} else {
				this.idle.setFlip(false);
				this.idle.run();
				return;
			} 
		}
		
		if(this.state.isTurnRunLeft()) {
			this.idle.setFlip(true);
			this.idle.run();
			return;
		}
		
		if(this.state.isTurnRunRight()) {
			this.idle.setFlip(false);
			this.idle.run();
			return;
		}
		
		if(this.state.isMoving()) {
			if(motionX > 0) {
				this.idle.setFlip(false);
				this.idle.run();
				return;
			}
			
			if(motionX < 0) {
				this.idle.setFlip(true);
				this.idle.run();
				return;
			}
		}
		
		this.idle.run();
	}*/
	
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
	
	public double nextRestricted(double motionX) { //Restricted growth model for player movement
		//System.out.println(-0.1*(motionX - maxMotionX));
		//System.out.println(this.motionX);
		return -0.01*(motionX - maxMotionX);
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
	
	protected void fall() {
		if(falling) {
			super.fall();
		}
	}
}
