package com.omg.entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.omg.main.Game;
import com.omg.world.World;

public class Enemy extends Entity {

	private int speed = 1;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}
	
	public void dumbChase() {
		if(Game.player.getX() > this.getX() && World.isFree(this.getX()+speed, this.getY()) && !isColliding(this.getX()+speed, this.getY())) {
			this.setX(this.getX()+speed);
		}else if(this.getX() > Game.player.getX() && World.isFree(this.getX()-speed, this.getY()) && !isColliding(this.getX()-speed, this.getY())) {
			this.setX(this.getX()-speed);
		}
		
		if(Game.player.getY() > this.getY() && World.isFree(this.getX(), this.getY()+speed) && !isColliding(this.getX(), this.getY()+speed)) {
			this.setY(this.getY()+speed);
		}else if(Game.player.getY() < this.getY() && World.isFree(this.getX(), this.getY()-speed) && !isColliding(this.getX(), this.getY()-speed)) {
			this.setY(this.getY()-speed);
		} 
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
	
	public void tick() {
		if(Game.rand.nextInt(100) < 70)
			dumbChase();
	}

}
