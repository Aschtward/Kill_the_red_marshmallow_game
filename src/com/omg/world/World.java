package com.omg.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import com.omg.entities.*;
import com.omg.graph.Spritesheet;
import com.omg.graph.UI;
import com.omg.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int width, height;
	public static int tile_size = 32;

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
						tiles[i + (j *width)] = new Floor(i*tile_size,j*tile_size,Tile.tile_floor);
						
						if(pxAtual == 0xFF000000) {
							tiles[i + (j *width)] = new Floor(i*tile_size,j*tile_size,Tile.tile_floor);
						}else if(pxAtual == 0xFFFFFFFF) {
							tiles[i + (j *width)] = new Wall(i*tile_size,j*tile_size,Tile.tile_wall);
						}else if(pxAtual == 0xFF0026FF) {
							Game.player.setX(i*tile_size);
							Game.player.setY(j*tile_size);
						}else if(pxAtual == 0xFFFF0800){
							//Enemy
							Enemy en = new Enemy(i*tile_size,j*tile_size,tile_size,tile_size,Entity.enemy);
							Game.entities.add(en);
							Game.enemies.add(en);
						}else if(pxAtual == 0xFF808080) {
							//arma
							Game.entities.add(new Gun(i*tile_size,j*tile_size,tile_size,tile_size,Entity.gun_right));
						}else  if(pxAtual == 0xFF00FF1D) {
							//heal
							Game.entities.add(new Heal(i*tile_size,j*tile_size,tile_size,tile_size,Entity.heal));
						}else if(pxAtual == 0xFFFFDD00) {
							//bullet
							Game.entities.add(new Bullet(i*tile_size,j*tile_size,tile_size,tile_size,Entity.bullet));
						}
					}
				}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void worldRestart(String path) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.image = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_RGB);
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/text.png");
		Game.player = new Player(0,0,0,0, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World(path);
		Game.ui = new UI();
	}
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / tile_size;
		int y1 = ynext/ tile_size;
		
		int x2 = (xnext+tile_size-1) / tile_size;
		int y2 = ynext/ tile_size;
		
		int x3 = xnext / tile_size;
		int y3 = (ynext+tile_size-1)/ tile_size;
		
		int x4 = (xnext+tile_size-1) / tile_size;
		int y4 = (ynext+tile_size-1)/ tile_size;
		
		return !(tiles[x1+(y1*World.width)] instanceof Wall ||
				tiles[x2+(y2*World.width)] instanceof Wall ||
				tiles[x3+(y3*World.width)] instanceof Wall ||
				tiles[x4+(y4*World.width)] instanceof Wall );
	}
	public void render(Graphics g) {
		int xstart = Camera.x / tile_size;
		int ystart = Camera.y / tile_size;
		int xfinal = xstart  + (Game.WIDTH / tile_size);
		int yfinal = ystart + (Game.HEIGHT/ tile_size);
		for(int i = xstart; i <= xfinal + 1; i++) {
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
