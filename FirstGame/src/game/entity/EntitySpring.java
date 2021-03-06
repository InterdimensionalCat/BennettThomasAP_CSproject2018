package game.entity;

import java.awt.Rectangle;

import game.Game;
import game.render.textures.Texture;
import game.utils.init.InitAudio;
import game.world.TileMap;

public class EntitySpring extends Entity {

	private boolean horz;
	
	public EntitySpring(Texture texture, double x, double y, TileMap tileMap, boolean horz) {
		super(texture, x, y, tileMap, new Rectangle((int)x, (int)y, 64, 64));
		if(horz) {
			this.texture.rotateImage(-Math.PI / 2);
		}
		this.horz = horz;
		
	}

	@Override
	public void tick() {
		if(horz) {
			if(tileMap.getPlayer().getAABB().intersects(this.AABB)&&tileMap.getPlayer().getX() > this.x) {
				if(true) {
					if(tileMap.getPlayer().getSpeed() > 16) {
						tileMap.getPlayer().multSpeed(1.1);
						if(!InitAudio.musicFiles.get("BoopDeath").isPlaying()) {
							Game.fxmanager.playSound("BoopDeath");
						}
					} else {
						tileMap.getPlayer().setSpeed(16);
						if(!InitAudio.musicFiles.get("BoopDeath").isPlaying()) {
							Game.fxmanager.playSound("BoopDeath");
						}
					}
				}
			}
		} else {
			if(tileMap.getPlayer().getAABB().intersects(this.AABB)) {
				if(true) {
					tileMap.getPlayer().falling = true;
					tileMap.getPlayer().ysp = -16;
					if(!InitAudio.musicFiles.get("BoopDeath").isPlaying()) {
						Game.fxmanager.playSound("BoopDeath");
					}
				}
			}
		}

		
	}

	@Override
	public void setDead() {
		this.AABB = null;
		
	}

}
