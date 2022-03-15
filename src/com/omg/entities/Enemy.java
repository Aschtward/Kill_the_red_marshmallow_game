package com.omg.entities;

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
		if(Game.player.getX() > this.getX() && World.isFree(this.getX()+speed, this.getY())) {
			this.setX(this.getX()+speed);
		}else if(this.getX() > Game.player.getX() && World.isFree(this.getX()-speed, this.getY())) {
			this.setX(this.getX()-speed);
		}
		
		if(Game.player.getY() > this.getY() && World.isFree(this.getX(), this.getY()+speed)) {
			this.setY(this.getY()+speed);
		}else if(Game.player.getY() < this.getY() && World.isFree(this.getX(), this.getY()-speed)) {
			this.setY(this.getY()-speed);
		} 
	}
	
	public void tick() {
		dumbChase();
	}

}
