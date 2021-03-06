package game.render;

import java.awt.Graphics2D;

import game.Game;
import game.render.textures.Texture;

public class ParallaxLayer {

	private Texture texture;
	private double x, y;
	private int width, height;
	private double motionX;
	private int gap;
	private boolean left, right;
	
	public ParallaxLayer(Texture texture, double motionX, int gap) { //assumes texture is the screensize
		this.texture = texture;
		this.motionX = motionX;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.gap = gap;
		this.x = 0;
		this.y = 0;
	}
	
	public ParallaxLayer(Texture texture, double motionX) {
		this(texture, motionX, 0);
	}
	
	public void setRight() {
		right = true;
		left = false;
	}
	
	public void setLeft() {
		right = false;
		left = true;
	}
	
	public void stop() {
		right = false;
		left = false;
	}
	
	public void move(double Xmodifier) {
		if(Xmodifier < 1) {
			Xmodifier = 0;
		}
		double dx = (motionX*Xmodifier/15);
		if(right) {
			x = (x + dx) % (width+gap);
		} else {
			x = (x - dx) % width; //not using left?
		}
	}
	
	public void render(Graphics2D g2d) {
		if(x == 0) {
			texture.render(g2d, 0, Game.WIDTH /**Game.SCALEFACTOR*/, 0, Game.WIDTH /**Game.SCALEFACTOR*/, y);
		} else {
			if (x > 0 && x < Game.WIDTH /**Game.SCALEFACTOR*/) {
				texture.render(g2d, x, Game.WIDTH /**Game.SCALEFACTOR*/, 0, Game.WIDTH /**Game.SCALEFACTOR*/ - x, y);
				texture.render(g2d, 0, x, width - x, Game.WIDTH /**Game.SCALEFACTOR*/, y);
			} else {
				if(x >= Game.WIDTH /**Game.SCALEFACTOR*/) {
					texture.render(g2d, 0, Game.WIDTH /**Game.SCALEFACTOR*/, width - x, width - x + Game.WIDTH /**Game.SCALEFACTOR*/, y);
				} else {
					if(x < 0 && x>= Game.WIDTH /**Game.SCALEFACTOR*/ - width) {
						texture.render(g2d, 0, Game.WIDTH /**Game.SCALEFACTOR*/, -x, Game.WIDTH /**Game.SCALEFACTOR*/ - x, y);
					} else {
						if(x < Game.WIDTH /**Game.SCALEFACTOR*/ - width) {
							texture.render(g2d, 0, width + x, -x, width, y);
							texture.render(g2d, gap + width + x, gap + Game.WIDTH /**Game.SCALEFACTOR*/, 0, Game.WIDTH /**Game.SCALEFACTOR*/ - width - x, y);
						}
					}
				}
			}
		}
	}
}
