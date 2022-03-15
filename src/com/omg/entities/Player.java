package com.omg.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.omg.main.Game;
import com.omg.world.Camera;
import com.omg.world.World;

public class Player extends Entity{
	
	public boolean right, left, up, down;
	public int speed = 2;
	
	private BufferedImage[] rightPlayer, leftPlayer;
	private int frames;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		
		rightPlayer[0] = Game.spritesheet.getSprite(32, 0, 16, 16);
		rightPlayer[1] = Game.spritesheet.getSprite(48, 0, 16, 16);
		leftPlayer[0] = Game.spritesheet.getSprite(64, 0, 16, 16);
		leftPlayer[1] = Game.spritesheet.getSprite(0, 16, 16, 16);
	}
	
	
	
	public void tick() {
		if(right && World.isFree(getX()+speed, getY())) {
			setX(getX() + speed);
		}
		if(left && World.isFree(getX()-speed, getY())) {
			setX(getX() - speed);
		}
		if(up && World.isFree(getX(), getY()-speed)) {
			setY(getY() - speed);
		}
		if(down && World.isFree(getX(), getY()+speed)) {
			setY(getY() + speed);
		}
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.width*32 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.WIDTH/2),0,World.height*32 - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		
		if(down) {
			g.drawImage(rightPlayer[0],this.getX() - Camera.x,this.getY()- Camera.y,32,32,null);
		}else if(left) {
			g.drawImage(leftPlayer[1],this.getX()- Camera.x,this.getY()- Camera.y,32,32,null);
		}else if(right){
			g.drawImage(leftPlayer[0],this.getX()- Camera.x,this.getY()- Camera.y,32,32,null);
		}else if(up) {
			g.drawImage(rightPlayer[1],this.getX()- Camera.x,this.getY()- Camera.y,32,32,null);
		}else {
			g.drawImage(rightPlayer[0],this.getX()- Camera.x,this.getY()- Camera.y,32,32,null);
		}
	}

}
