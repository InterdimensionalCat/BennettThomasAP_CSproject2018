package game.render.textures;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import game.Game;

public class Texture {
	
	private static final Map<String, BufferedImage> textureMap = new HashMap<String, BufferedImage>();
	private BufferedImage image;
	private String fileName;
	private String uniqueName;
	private int width, height;
	private boolean flip;
	private boolean hasFlip;
	public static int windowPosX;
	public static int windowPosY;
	
	public Texture(String fileName) {
		this.fileName = fileName;
		BufferedImage oldImage = textureMap.get(fileName);
		if(oldImage != null) {
			image = oldImage;
			this.height = oldImage.getHeight();
			this.width = oldImage.getWidth();
		} else {
			try {
				System.out.println("Loading Texture:" + fileName);
				//image = ImageIO.read(new File("src/assets/" + fileName + ".png")); //need to do something more like this: || this.image = ImageIO.read(Texture.class.getResourceAsStream("/textures/" + fileName + ".png"));
				this.image = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream("assets/" + fileName + ".png"));
				textureMap.put(fileName, image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.height = image.getHeight();
			this.width = image.getWidth();
		}
		uniqueName = fileName;
		
		
	}
	
	public Texture(Texture spriteSheet, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		String key = spriteSheet.fileName + "_" + x + "_" + y;
		BufferedImage oldImage = textureMap.get(key);
		if(oldImage != null) { //SWITCH TO HASKEY
			image = oldImage;
			} else {
				this.image = spriteSheet.image.getSubimage(x*width - width, y * height - height, width, height);
				System.out.println("Calculating image: " + key);
			}
		uniqueName = key;
		
	}
	
	public Texture(Texture spriteSheet, int x, int y, int width, int height, boolean hasFlip) {
		this(spriteSheet, x, y, width, height);
		this.hasFlip = hasFlip;
	}
	
	public Texture(Texture spriteSheet, int x, int y, int size, boolean hasFlip) {
		this(spriteSheet, x, y, size);
		this.hasFlip = hasFlip;
	}
	
	public Texture(Texture spriteSheet, int x, int y, int size) {
		this(spriteSheet,  x,  y, size, size);
	}
	
	public void render(Graphics g, double posX, double posY) {
		
		if(posX > windowPosX + Game.WIDTH + 64 || posX < windowPosX -64|| posY > windowPosY + Game.HEIGHT + 64|| posY < windowPosY - 64) {
			return;
		}
		
		if(!hasFlip) {
			g.drawImage(image, (int) posX, (int) posY, this.width, this.height, null); //old drawing method, would not allow image to be easily flipped
			 
		} else {
			if(flip) {
				g.drawImage(image, (int)posX, (int)posY /**Game.SCALEFACTOR*/, (int)(posX + width) /**Game.SCALEFACTOR*/, (int)(posY + height)/**Game.SCALEFACTOR*/, (int)0 + width, (int)0, (int)0 ,(int) 0 + height, null);
			} else {
				g.drawImage(image, (int)posX, (int)posY /**Game.SCALEFACTOR*/, (int)(posX + width) /**Game.SCALEFACTOR*/, (int)(posY + height)/**Game.SCALEFACTOR*/, (int)0, (int)0, (int)0 + width ,(int) 0 + height, null);
			}
		}
	}
	
	public void render(Graphics g, double posX, double posY, double angle) {
		
		if(posX > windowPosX + Game.WIDTH + 64 || posX < windowPosX -64|| posY > windowPosY + Game.HEIGHT + 64|| posY < windowPosY - 64) {
			return;
		}
		
		if(!hasFlip) {
			g.drawImage(this.rotateImage(angle), (int) posX, (int) posY, this.width, this.height, null); //old drawing method, would not allow image to be easily flipped
			 
		} else {
			if(flip) {
				
				if(angle == Math.PI || angle == 0) {
					g.drawImage(this.rotateImage(angle), (int)posX, (int)posY /**Game.SCALEFACTOR*/, (int)(posX + width) /**Game.SCALEFACTOR*/, (int)(posY + height)/**Game.SCALEFACTOR*/, (int)0 + width, (int)0, (int)0 ,(int) 0 + height, null);
				} else {
					g.drawImage(this.rotateImage(angle), (int)posX, (int)posY /**Game.SCALEFACTOR*/, (int)(posX + width) /**Game.SCALEFACTOR*/, (int)(posY + height)/**Game.SCALEFACTOR*/, (int)0, (int)0 + height, (int)0 + width ,(int)0, null);
				}
			} else {
				g.drawImage(this.rotateImage(angle), (int)posX, (int)posY /**Game.SCALEFACTOR*/, (int)(posX + width) /**Game.SCALEFACTOR*/, (int)(posY + height)/**Game.SCALEFACTOR*/, (int)0, (int)0, (int)0 + width ,(int) 0 + height, null);
			}
		}
	}
	
	public void render(Graphics2D g2d, double toX1, double toX2, double posX1, double posX2, double y) {
		
/*		if(posX > windowPosX + Game.WIDTH || posX < windowPosX || posY > windowPosY + Game.HEIGHT || posY < windowPosY) {
			return;
		}*/
		
		g2d.drawImage(image, (int)toX1/**Game.SCALEFACTOR*/, (int)y/**Game.SCALEFACTOR*/, (int)toX2/**Game.SCALEFACTOR*/, (int)y + height, (int)posX1/**Game.SCALEFACTOR*/, 0, (int)posX2/**Game.SCALEFACTOR*/, height, null);
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public void setBufferedImage(BufferedImage bIn) {
		this.image = bIn;
	}
	
	public String getName() {
		return uniqueName;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setFlip(boolean flip) {
		this.flip = flip;
	}
	
	public boolean getFlip() {
		return flip;
	}
	
	/**
	 * 
	 * @param angle (angle in radians)
	 */
	
	public BufferedImage rotateImage(double angle) {

		BufferedImage imag = this.getImage();
        double rotationRequired = angle;
        double locationX = imag.getWidth() / 2;
        double locationY = imag.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);         
       BufferedImage newImage = new BufferedImage(imag.getWidth(), imag.getHeight(), imag.getType());
       op.filter(imag, newImage);

       return newImage;
     }
	
/*	public BufferedImage resizeTexture(int sf){
		BufferedImage resizedImage = new BufferedImage(this.image.getWidth()*sf, this.image.getHeight()*sf, this.image.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(this.image, 0, 0, this.image.getWidth()*sf, this.image.getHeight()*sf, null);
		g.dispose();
		
		return resizedImage;
	    }*/

	
}
