package game.render.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import game.Game;
import game.utils.Fonts;

public class Button extends Rectangle{

	private Font font, selectedFont;
	private Color color, selectedColor;
	private boolean selected;
	private String text;
	private int textY; //Texts draws from bottom left instead of top left
	public Button(String text, int textY, Font font, Font selectedFont, Color color, Color selectedColor) {
		this.text = text;
		this.textY = textY;
		this.font = font;
		this.selectedFont = selectedFont;
		this.color = color;
		this.selectedColor = selectedColor;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void render(Graphics g) {
		if(selected) {
			Fonts.drawString(g, selectedFont, selectedColor, text, textY);
		} else {
			Fonts.drawString(g, font, color, text, textY);
		}
		
		FontMetrics metrics = g.getFontMetrics();
		this.x = (Game.WIDTH/**Game.SCALEFACTOR*/ - metrics.stringWidth(text)) / 2;
		this.y = textY - metrics.getHeight();
		this.width = metrics.stringWidth(text);
		this.height = metrics.getHeight();
		//g.drawRect(x, y, width, height);
	}
	
}
