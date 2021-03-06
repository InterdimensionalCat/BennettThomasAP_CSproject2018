package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import game.Game;
import game.input.KeyInput;
import game.input.MouseInput;
import game.render.ui.Button;
import game.utils.Fonts;
import game.utils.init.InitAudio;

public class Options implements State {

	private Button[] options; //selection 0, 1, or 2
	private int currentSelection;
	
	@Override
	public void init() {
		options = new Button[4];
		options[0] = new Button("VOLUME", 200 + 0 *80, 
				new Font("Arial", Font.PLAIN, 32), new Font("Arial", Font.BOLD, 48),
				Color.WHITE, Color.GRAY);
		options[1] = new Button("+", 200 + 1 *80, 
				new Font("Arial", Font.PLAIN, 32), new Font("Arial", Font.BOLD, 48),
				Color.WHITE, Color.GRAY);
		options[2] = new Button("-", 200 + 2 *80, 
				new Font("Arial", Font.PLAIN, 32), new Font("Arial", Font.BOLD, 48),
				Color.WHITE, Color.GRAY);
		options[3] = new Button("BACK", 200 + 3 *80, 
				new Font("Arial", Font.PLAIN, 32), new Font("Arial", Font.BOLD, 48),
				Color.WHITE, Color.GRAY);
		
	}

	@Override
	public void enter() {
		Game.player.stop();
	}

	public Options() {

	}
	
	@Override
	public void tick(StateManager stateManager) {
		
		if(KeyInput.wasPressed(KeyEvent.VK_UP)||KeyInput.wasPressed(KeyEvent.VK_W)) {
			currentSelection--;
			if(currentSelection < 0) {
				currentSelection = options.length - 1;
			}
		}
		
		if(KeyInput.wasPressed(KeyEvent.VK_DOWN)||KeyInput.wasPressed(KeyEvent.VK_S)) {
			currentSelection++;
			if(currentSelection >= options.length) {
				currentSelection = 0;
			}
		}
		
		boolean clicked = false;
		for(int i = 0; i < options.length; i++) {
			if(options[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1 , 1))) {
				currentSelection = i;
				clicked = MouseInput.wasPressed(MouseEvent.BUTTON1);
			}
			
		}
		
		if(clicked || KeyInput.wasPressed(KeyEvent.VK_ENTER)) {
			select(stateManager);
		}
	}
	
	private void select(StateManager stateManager) {
		switch(currentSelection) {
		case 0:
			Game.fxmanager.playSound("PlayerDead");
			break;
			
		case 1:
			Game.fxmanager.globalFXVolume++;
			Game.player.globalBGMVolume++;
			break;
			
		case 2:
			Game.fxmanager.globalFXVolume--;
			Game.player.globalBGMVolume--;
			break;
		case 3:
			stateManager.setState("MENU");
		}
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH/**Game.SCALEFACTOR*/, Game.HEIGHT/**Game.SCALEFACTOR*/);
		Fonts.drawString(g, new Font("Arial", Font.BOLD, 72) , Color.WHITE, "Options", 80);
		Fonts.drawString(g, new Font("Arial", Font.BOLD, 48) , Color.WHITE, "Volume :" + Game.fxmanager.globalFXVolume, 200 + 4 *80);
		for(int i = 0; i < options.length; i++) {
			if (i == currentSelection) {
				options[i].setSelected(true);
			} else {
				options[i].setSelected(false);
			}
			options[i].render(g);
		}
	}
	
	
	
	@Override
	public void exit() {
		Game.player.start();
	}

	@Override
	public String getName() {

		return "OPTIONS";
	}
}
