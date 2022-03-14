package com.omg.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.omg.entities.Enemy;
import com.omg.entities.*;
import com.omg.main.Game;

public class World {
	
	private Tile[] tiles;
	public static int width, height;

	public World(String path) {
		
		try {
			BufferedImage mapa = ImageIO.read(getClass().getResource(path));
			int[] px = new int[mapa.getWidth() * mapa.getHeight()];
			width = mapa.getWidth();
			height = mapa.getHeight();
			tiles = new Tile[mapa.getWidth() * mapa.getHeight()];
			mapa.getRGB(0, 0, mapa.getWidth(), mapa.getHeight(), px ,0, mapa.getWidth());
			for(int i = 0; i <  mapa.getWidth(); i++) {
				
				for(int j = 0; j < mapa.getHeight();j++) {
					
						int pxAtual = px[i + (j*mapa.getWidth())];
						tiles[i + (j *width)] = new Floor(i*16,j*16,Tile.tile_floor);
						
						if(pxAtual == 0xFF000000) {
							tiles[i + (j *width)] = new Floor(i*16,j*16,Tile.tile_floor);
						}else if(pxAtual == 0xFFFFFFFF) {
							tiles[i + (j *width)] = new Floor(i*16,j*16,Tile.tile_wall);
						}else if(pxAtual == 0xFF0026FF) {
							Game.player.setX(i*16);
							Game.player.setY(j*16);
						}else if(pxAtual == 0xFFFF0800){
							//Enemy
							Game.entities.add(new Enemy(i*16,j*16,16,16,Entity.enemy));
						}else if(pxAtual == 0xFF808080) {
							//arma
							Game.entities.add(new Gun(i*16,j*16,16,16,Entity.gun));
						}else  if(pxAtual == 0xFF00FF1D) {
							//heal
							Game.entities.add(new Heal(i*16,j*16,16,16,Entity.heal));
						}else if(pxAtual == 0xFFFFDD00) {
							//bullet
							Game.entities.add(new Bullet(i*16,j*16,16,16,Entity.bullet));
						}
					}
				}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		int xfinal = xstart  + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int i = xstart; i <= xfinal +  1; i++) {
			for(int j = ystart; j <= yfinal + 1;  j++) {
				if(i < 0 || j < 0 || i >= width|| j >= height) {
					continue;
				}
				Tile tile = tiles[i+ (j*width)];
				tile.render(g);
			}
		}
	}

}
