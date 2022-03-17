package com.omg.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.omg.main.Game;
import com.omg.world.Camera;

public class Entity {
	
	private int x;
	private int y;
	private int width;
	private int height;
	private BufferedImage sprite;	
	public static BufferedImage heal = Game.spritesheet.getSprite(5*16,0,16,16);
	public static BufferedImage gun = Game.spritesheet.getSprite(6*16,0,16,16);
	public static BufferedImage bullet = Game.spritesheet.getSprite(7*16,0,16,16);
	public static BufferedImage enemy = Game.spritesheet.getSprite(8*16, 0, 16, 16);
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y,32,32,null);
	}
	
	public boolean collided() {
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),32,32);
		Rectangle ent = new Rectangle(this.getX(),this.getY()+8,10,10);
		return player.intersects(ent);
	}
	public void tick(){
		
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
		
}
