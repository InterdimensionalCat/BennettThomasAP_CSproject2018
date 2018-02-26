package game.utils;

public class ChildThread extends Thread {
	
	private static final UUID threadID = new UUID(1);
	
	private ThreadManager pool;

	public ChildThread(ThreadManager pool) {
		super(pool, "ChildThread-" + threadID.next());
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
