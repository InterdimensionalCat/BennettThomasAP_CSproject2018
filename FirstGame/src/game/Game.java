package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import game.audio.MusicPlayer;
import game.audio.SoundFXManager;
import game.audio.SoundFXPlayer;
import game.input.KeyInput;
import game.input.MouseInput;
import game.render.textures.Texture;
import game.render.ui.SplashScreenDriver;
import game.states.GameState;
import game.states.MenuState;
import game.states.StateManager;
import game.utils.ThreadPool;
import game.utils.Util;
import game.utils.init.InitAnimations;
import game.utils.init.InitAudio;
import game.utils.init.InitLevels;
import game.utils.init.InitTextures;
import game.world.Tile;

public class Game extends Canvas implements Runnable {

	public static final String TITLE = "Game and Watch";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 960;
	private boolean running;
	private StateManager stateManager;
	public static Game INSTANCE;
	public static boolean debug = false;
	public static boolean showTileMap = false;
	public static GameState level;
	public static volatile SplashScreenDriver driver;
	public static volatile int taskComplete;
	public static volatile SoundFXManager fxmanager;
	public static int pauseTime;
	public static KeyInput keyInput;
	
	public Game() {
		keyInput = new KeyInput();
		addKeyListener(keyInput);
		MouseInput mi = new MouseInput();
		addMouseListener(mi);
		addMouseMotionListener(mi);
		setStateManager(new StateManager());
		
		stateManager.addState(new MenuState());
		stateManager.addState(new GameState());
		
		INSTANCE = this;
	}
	
	private void tick() {
		getStateManager().tick();
		if (KeyInput.wasPressed(KeyEvent.VK_I)) {
			debug = !debug;
		}
		if (KeyInput.wasPressed(KeyEvent.VK_M)) {
			showTileMap = !showTileMap;
		}
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		/////////////////////////////////////
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		getStateManager().render(g2d);
		
		/////////////////////////////////////
		g.dispose();
		bs.show();
	}
	
/*	private void start() { // commented out for multithreading
		if(running) return;
		running = true;
		new Thread(this, "GandW-GameLoop").start();
	}*/
	
	public void stop() {
		if(!running) return;
		running = false;
	}
	
	
	@Override
	public void run() {
		running = true; // for multithreading
		requestFocus();
		double target = 60.0;
		double nsPerTick = 1000000000.0 / target;
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double unprocessed = 0.0;
		int fps = 0;
		int tps = 0;
		boolean canRender = false;
		System.out.println("Running");
		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			if(unprocessed >= 1.0) {
				if(pauseTime <= 0) {
					tick();
				} else {
					pauseTime--;
					keyInput.clear();
				}
				KeyInput.update();
				MouseInput.update();
				unprocessed--;
				tps++;
				canRender = true;
			} else canRender = false;
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(canRender) {
				render();
				fps++;
			}
			
			if(System.currentTimeMillis() - 1000 > timer) {
				timer += 1000;
				if(Game.debug) {
					//System.out.printf("FPS: %d | TPS: %d\n", fps, tps);
					System.out.printf("TPS: %d\n", tps);
				}
				fps = 0;
				tps = 0;
			}
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		driver = new SplashScreenDriver();
		System.out.println("Avalible Processors: " + Runtime.getRuntime().availableProcessors());
		ThreadPool pool = new ThreadPool(3);
		pool.runTask(new InitTextures());
		pool.runTask(new InitAnimations());
		pool.runTask(new InitLevels());
		pool.runTask(new InitAudio());
		//pool.join();
		
		System.out.println("Running on OS: " + Util.getOSName());
		while(taskComplete < 4) {
			try {
				//driver.getScreen().setProgress((taskComplete + 1) * 33);
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		final Game game = new Game();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.err.println("Exiting Game");
				game.stop();
			}
		});
		
		frame.getContentPane().setPreferredSize(new Dimension(1280, 960));
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		MusicPlayer player = new MusicPlayer("FlatZone"); //music player playlist initialization
		fxmanager = new SoundFXManager(3);
		pool.runTask(player);
		pool.runTask(game);
		//game.start(); commented out for multithreading
		pool.join();
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}

}
