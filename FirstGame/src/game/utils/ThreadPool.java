package game.utils;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool extends ThreadGroup {
	
	private boolean alive;
	private List<Runnable> taskQueue;
	private int id;
	
	private static final UUID poolID = new UUID(1);

	public ThreadPool(int numThreads) {
		super("ThreadPool-" + poolID.next());
		this.id = poolID.getCurrentID();
		setDaemon(true);
		taskQueue = new LinkedList<Runnable>();
		alive = true;
		for(int i = 0; i < numThreads; i++) {
			new PooledThread(this).start();
		}
	}

	public synchronized void runTask(Runnable task) {
		if(!alive) {
			throw new IllegalStateException("ThreadPool-" + id + " is dead");
		}
		if (task != null) {
			taskQueue.add(task);
			notify();
		}
	}
	
	public synchronized void close() {
		if(!alive) {
			return;
		}
		alive = false;
		taskQueue.clear();
		interrupt();
	}
	
	public void join() {
		synchronized(this) {
			alive = false;
			notifyAll();
		}
		
		Thread[] threads = new Thread[activeCount()];
		int count = enumerate(threads);
		for(int i = 0; i < count; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				//e.printStackTrace();
				System.err.println("Thread could not be joined");
			}
		}
	}
	
	protected synchronized Runnable getTask() throws InterruptedException {
		while(taskQueue.size() == 0) {
			if(!alive) {
				return null;
			}
			wait();
		}
		return taskQueue.remove(0);
	}
	
}
