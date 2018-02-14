package game.render.textures;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Texture {
	
	private static final Map<String, BufferedImage> textureMap = new HashMap<String, BufferedImage>();
	private BufferedImage image;
	private String fileName;
	private int width, height;
	
	public Texture(String fileName) {
		this.fileName = fileName;
		BufferedImage oldImage = textureMap.get(fileName);
		if(oldImage != null) {
			image = oldImage;
		} else {
			try {
				System.out.println("Loading Texture:" + fileName);
				image = ImageIO.read(new File("src/assets/" + fileName + ".png")); //need to do something more like this: || this.image = ImageIO.read(Texture.class.getResourceAsStream("/textures/" + fileName + ".png"));
				textureMap.put(fileName, image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.height = image.getHeight();
			this.width = image.getWidth();
		}
		
		
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
	}
	
	public Texture(Texture spriteSheet, int x, int y, int size) {
		this(spriteSheet,  x,  y, size, size);
	}
	
	public void render(Graphics g, double posX, double posY) {
		g.drawImage(image, (int) posX, (int) posY , null);
	}
	
	public void render(Graphics2D g2d, double toX1, double toX2, double posX1, double posX2, double y) {
		g2d.drawImage(image, (int)toX1, (int)y, (int)toX2, (int)y + height, (int)posX1, 0, (int)posX2, height, null);
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	

	
}
