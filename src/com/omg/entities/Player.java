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
	private static BufferedImage playerDamage = Game.spritesheet.getSprite(3*16, 16, 16, 16);
	public boolean gotDamage = false;
	private int damf = 0;
	public double life = 100;
	public double maxlife = 100;
	public int ammo = 0;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		
		rightPlayer[0] = Game.spritesheet.getSprite(32, 0, 16, 16);
		rightPlayer[1] = Game.spritesheet.getSprite(48, 0, 16, 16);
		leftPlayer[0] = Game.spritesheet.getSprite(64, 0, 16, 16);
		leftPlayer[1] = Game.spritesheet.getSprite(0, 16, 16, 16);
	}
	
	
	public void gotLife() {
		for(int i = 0; i < Game.entities.size(); i++) {
			if(Game.entities.get(i) instanceof Heal) {
				if(Game.entities.get(i).collided()) {
					Game.entities.remove(i);
					this.life +=10;
					if(this.life > 100)
						this.life =100;
				}
			}if(Game.entities.get(i) instanceof Bullet) {
				if(Game.entities.get(i).collided()) {
					ammo += 5;
					Game.entities.remove(i);
				}
			}
		}
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
		
		this.gotLife();
		
		if(this.gotDamage) {
			damf++;
			if(this.damf == 8) {
				this.damf = 0;
				this.gotDamage = false;
			}
		}
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.width*World.tile_size - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.WIDTH/2),0,World.height*World.tile_size - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		
		if(!this.gotDamage) {
			if(down) {
				g.drawImage(rightPlayer[0],this.getX() - Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
			}else if(left) {
				g.drawImage(leftPlayer[1],this.getX()- Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
			}else if(right){
				g.drawImage(leftPlayer[0],this.getX()- Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
			}else if(up) {
				g.drawImage(rightPlayer[1],this.getX()- Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
			}else {
				g.drawImage(rightPlayer[0],this.getX()- Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
			}
		}else {
			g.drawImage(playerDamage,this.getX() - Camera.x,this.getY() - Camera.y,World.tile_size,World.tile_size,null);
		}
	}

}
