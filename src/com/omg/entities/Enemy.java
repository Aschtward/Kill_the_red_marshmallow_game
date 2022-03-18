package com.omg.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import com.omg.graph.Spritesheet;
import com.omg.graph.UI;
import com.omg.main.Game;
import com.omg.world.Camera;
import com.omg.world.World;

public class Enemy extends Entity {

	private int speed = 1;
	private BufferedImage[] ani;
	private int direction = 0;
	private int view_size = 200;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		ani = new BufferedImage[4];
		ani[0] = Game.spritesheet.getSprite((16*9), 0, 16, 16);//up
		ani[1] = Game.spritesheet.getSprite((16*8), 0, 16, 16);//down
		ani[2] = Game.spritesheet.getSprite(16, 16, 16, 16);//right
		ani[3] = Game.spritesheet.getSprite((16*2), 16, 16, 16);//left
	}
	
	public void dumbChase() {
		if(Game.player.getX() > this.getX() && World.isFree(this.getX()+speed, this.getY()) && !isColliding(this.getX()+speed, this.getY())) {
			this.setX(this.getX()+speed);
			this.direction = 2;
		}else if(this.getX() > Game.player.getX() && World.isFree(this.getX()-speed, this.getY()) && !isColliding(this.getX()-speed, this.getY())) {
			this.setX(this.getX()-speed);
			this.direction = 3;
		}
		
		if(Game.player.getY() > this.getY() && World.isFree(this.getX(), this.getY()+speed) && !isColliding(this.getX(), this.getY()+speed)) {
			this.setY(this.getY()+speed);
			this.direction = 1;
		}else if(Game.player.getY() < this.getY() && World.isFree(this.getX(), this.getY()-speed) && !isColliding(this.getX(), this.getY()-speed)) {
			this.setY(this.getY()-speed);
			this.direction = 0;
		} 
	}
	public boolean isCollidingPlayer() {
		Rectangle enemy = new Rectangle(this.getX(),this.getY(),World.tile_size - 6,World.tile_size - 6);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(),World.tile_size,World.tile_size);
		return enemy.intersects(player);
	}
	
	public boolean isColliding(int xnext,int ynext) {
		Rectangle enemy = new Rectangle(xnext,ynext,World.tile_size - 6,World.tile_size - 6);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle collidingEnemy = new Rectangle(e.getX(),e.getY(),World.tile_size - 6,World.tile_size - 6);
			if(enemy.intersects(collidingEnemy))
				return true;
		}
		return false;
	}
	
	public boolean isSeeing() {
		Rectangle enemyView = new Rectangle(this.getX()-(int)(view_size/2),this.getY()-(int)(view_size/2),view_size,view_size);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(),World.tile_size,World.tile_size);
		return enemyView.intersects(player);
	}
	
	public void tick() {
		if(!isCollidingPlayer()) {
			if(isSeeing()) {
				if(Game.rand.nextInt(100) < 70)
					dumbChase();
			}
		}else {
			if(Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(5);
				Game.player.gotDamage = true;
					if(Game.player.life <= 0) {
						Game.image = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_RGB);
						Game.entities = new ArrayList<Entity>();
						Game.enemies = new ArrayList<Enemy>();
						Game.spritesheet = new Spritesheet("/text.png");
						Game.player = new Player(0,0,0,0, Game.spritesheet.getSprite(32, 0, 16, 16));
						Game.entities.add(Game.player);
						Game.world = new World("/map.png");
						Game.ui = new UI();
					}
			}
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(ani[direction], this.getX() - Camera.x, this.getY() - Camera.y,World.tile_size,World.tile_size,null);
	}

}
