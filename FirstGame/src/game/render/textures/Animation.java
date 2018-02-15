package game.render.textures;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Animation {

	private int count;
	private int index;
	private int speed;
	private int numFrames;
	private Texture currentFrame;
	private Texture[] frames;
	
	public Animation(int speed, Texture... frames) {
		this.speed = speed;
		this.frames = frames;
		this.numFrames = frames.length;
		this.currentFrame = frames[0];
	}
	
	private void nextFrame() {
		currentFrame = frames[index++];
		if(index >= numFrames) {
			index = 0;
		}
	}
	
	public void run() {
		if(++count > speed) {
			count = 0;
			nextFrame();
		}
	}
	
	public void render(Graphics2D g2d, double x, double y) {
		if(currentFrame != null) {
			currentFrame.render((Graphics)g2d, x, y);
		}
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
