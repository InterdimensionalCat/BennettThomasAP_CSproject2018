package game.states;

import java.awt.Graphics2D;

import game.Game;
import game.utils.init.InitLevels;
import game.world.TileMap;

public class GameState implements State {
	
	private TileMap tileMap;
	private String currentLevel;
	private int levelInt;

	@Override
	public void init() {
		tileMap = InitLevels.tileMap;
		tileMap.load(InitLevels.levels.get(0));
		currentLevel = InitLevels.levels.get(0);
		tileMap.setEntityList(InitLevels.levelEntities.get(InitLevels.levels.get(0)));
		Game.level = this;
	}

	@Override
	public void enter() {
		tileMap.load(currentLevel);
	}

	@Override
	public void tick(StateManager stateManager) {
		tileMap.tick();
	}

	@Override
	public void render(Graphics2D g) {
		tileMap.render(g, Game.WIDTH, Game.HEIGHT);
	}

	@Override
	public void exit() {
		
	}
	
	public void nextLevel() {
		tileMap.unload();
		tileMap.load(InitLevels.levels.get(++levelInt));
		currentLevel = InitLevels.levels.get(levelInt);
		tileMap.setEntityList(InitLevels.levelEntities.get(InitLevels.levels.get(levelInt)));
	}

	@Override
	public String getName() {
		return InitLevels.levels.get(levelInt);
	}
	
	public String getCurrentLevel() {
		return currentLevel;
	}

}
