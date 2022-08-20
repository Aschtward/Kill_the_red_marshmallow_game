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
	private boolean hasGun = false;
	public boolean shooting = false, mouseShoot = false;
	private int damf = 0;
	public int mx,my;
	public double life = 100;
	public double maxlife = 100;
	public int ammo = 10;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		
		rightPlayer[0] = Game.spritesheet.getSprite(32, 0, 16, 16);
		rightPlayer[1] = Game.spritesheet.getSprite(48, 0, 16, 16);
		leftPlayer[0] = Game.spritesheet.getSprite(64, 0, 16, 16);
		leftPlayer[1] = Game.spritesheet.getSprite(0, 16, 16, 16);
	}
	
	public void gotBullet() {
		for(int i = 0; i < Game.entities.size(); i++) {
			if(Game.entities.get(i) instanceof Bullet) {
				if(Game.entities.get(i).collided()) {
					ammo += 15;
					Game.entities.remove(i);
				}
			}
		}
	}
	
	public int gotGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			if(Game.entities.get(i) instanceof Gun) {
				if(Game.entities.get(i).collided()) {
					this.setHasGun(true);
					Game.entities.remove(i);
					return 1;
				}
			}
		}
		return 0;
	}
	
	public void gotHeal() {
		for(int i = 0; i < Game.entities.size(); i++) {
			if(Game.entities.get(i) instanceof Heal) {
				if(Game.entities.get(i).collided()) {
					Game.entities.remove(i);
					this.life +=10;
					if(this.life > 100)
						this.life =100;
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
		
		this.gotHeal();
		this.gotGun();
		this.gotBullet();
		
		if(this.gotDamage) {
			damf++;
			if(this.damf == 8) {
				this.damf = 0;
				this.gotDamage = false;
			}
		}
		if(shooting) {
			shooting = false;
			if(isHasGun() && ammo > 0) {
				ammo--;
				int dx = 0;
				int dy = 0;
				int xoffset = 0;
				int yoffset = 0;
				if(right) {
					 dx = 1;
					 yoffset= World.tile_size/2;
					 xoffset = World.tile_size/2;
				}
				if(left) {
					 dx = -1;
					 yoffset= World.tile_size/2;
					 xoffset = -World.tile_size/2;
				}
				if(down) {
					dy = 1;
					xoffset = World.tile_size/2;
					yoffset = World.tile_size;
				}
				if(up) {
					dy = -1;
					xoffset = World.tile_size/2;
					yoffset = -World.tile_size;
				}
				if(!up && !down && !left && !right) {
					dy = 1;
					xoffset = 16;
					yoffset = 32;
				}
				BulletShoot bullet = new BulletShoot(this.getX() + xoffset,this.getY() + yoffset,6,6,null,dx,dy);
				Game.shots.add(bullet);
			}
		}
		if(mouseShoot) {
			mouseShoot = false;
			if(isHasGun() && ammo > 0) {
				ammo--;
				double angle = Math.atan2(my - (this.getY()+8 - Camera.y),mx+8 - (this.getX() - Camera.x));
				
				double dx = Math.cos(angle) ;
				double dy = Math.sin(angle);
				int xoffset = 0;
				int yoffset = 0;
				if(right) {
					 yoffset= World.tile_size/2;
					 xoffset = World.tile_size/2;
				}
				if(left) {
					 yoffset= World.tile_size/2;
					 xoffset = -World.tile_size/2;
				}
				if(down) {
					xoffset = World.tile_size/2;
					yoffset = World.tile_size;
				}
				if(up) {
					xoffset = World.tile_size/2;
					yoffset = 16;
				}
				if(!up && !down && !left && !right) {
					xoffset = 16;
					yoffset = 16;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX() + xoffset,this.getY() + yoffset,6,6,null,dx,dy);
				Game.shots.add(bullet);
			}
		}
		
		if(life < 0) {
			Game.gameState = "game_over";
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.width*World.tile_size - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.WIDTH/2),0,World.height*World.tile_size - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		
		if(!this.gotDamage) {
			if(down) {
				g.drawImage(rightPlayer[0],this.getX() - Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
				if(this.isHasGun()) {
					g.drawImage(Entity.gun_front_back, this.getX() - Camera.x + 16, this.getY() - Camera.y + 8, World.tile_size, World.tile_size,null);
				}
			}else if(left) {
				g.drawImage(leftPlayer[1],this.getX()- Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
				if(this.isHasGun()) {
					g.drawImage(Entity.gun_left, this.getX() - Camera.x - 10, this.getY() - Camera.y + 10, World.tile_size, World.tile_size,null);
				}
			}else if(right){
				g.drawImage(leftPlayer[0],this.getX()- Camera.x ,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
				if(this.isHasGun()) {
					g.drawImage(Entity.gun_right, this.getX() - Camera.x + 10, this.getY() - Camera.y + 10, World.tile_size, World.tile_size,null);
				}
			}else if(up) {
				g.drawImage(rightPlayer[1],this.getX()- Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
				if(this.isHasGun()) {
					g.drawImage(Entity.gun_front_back, this.getX() - Camera.x + 16, this.getY() - Camera.y + 8, World.tile_size, World.tile_size,null);
				}
			}else {
				g.drawImage(rightPlayer[0],this.getX()- Camera.x,this.getY()- Camera.y,World.tile_size,World.tile_size,null);
				if(this.isHasGun()) {
					g.drawImage(Entity.gun_front_back, this.getX() - Camera.x + 16, this.getY() - Camera.y + 8, World.tile_size, World.tile_size,null);
				}
			}
		}else {
			g.drawImage(playerDamage,this.getX() - Camera.x,this.getY() - Camera.y,World.tile_size,World.tile_size,null);
		}
	}

	public boolean isHasGun() {
		return hasGun;
	}

	public void setHasGun(boolean hasGun) {
		this.hasGun = hasGun;
	}

}
