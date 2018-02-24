package game.render.ui;

import game.render.textures.Texture;

public class SplashScreenDriver {

	private SplashScreen screen;
	
	public SplashScreenDriver() {
		screen = new SplashScreen(new Texture("SplashScreen"));
		screen.setLocationRelativeTo(null);
		screen.setMaxProgress(100);
		screen.setVisible(true);
		
/*		for (int i = 0; i < 1000; i++) {
			for(int j = 0; j <= 50000; j++) {
				String s = "ewf" +(i + j);
			}
			screen.setProgress(i);
		}*/
	}
	
	public void setInvisible() {
		screen.setVisible(false);
	}
	
	public SplashScreen getScreen() {
		return screen;
	}
}
