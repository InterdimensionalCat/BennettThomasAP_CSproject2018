package game.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PooledThread extends Thread {
	
	private static final UUID threadID = new UUID(1);
	
	private ThreadPool pool;

	public PooledThread(ThreadPool pool) {
		super(pool, "PooledThread-" + threadID.next());
		this.pool = pool;
	}
	
	@Override
	public void run() {
		while(!isInterrupted()) {
			Runnable task = null;
			try {
				task = pool.getTask();
			} catch (InterruptedException e) {
				//Logger.getLogger(PooledThread.class.getName()).log(Level.SEVERE, null, e); the same as printStackTrace
				e.printStackTrace();
			}
			if(task == null) {
				return;
			}
			try {
			    task.run();
			} catch (Throwable t) {
				pool.uncaughtException(this, t);
			}
		}
	}
}
