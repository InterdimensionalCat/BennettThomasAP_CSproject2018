package game.utils.init;

public class Test2 implements Runnable {

	@Override
	public void run() {
		System.err.println("Test 2 started");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println("Test 2 ended");
	}

}
