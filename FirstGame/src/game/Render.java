package game;

public class Render implements Runnable {

	@Override
	public void run() {
		Game.INSTANCE.render(Game.interpol);
	}

}
