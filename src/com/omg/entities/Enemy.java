package com.omg.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.omg.main.Game;
import com.omg.world.Camera;
import com.omg.world.World;

public class Enemy extends Entity {

	private int speed = 1;
	private BufferedImage[] ani;
	private int direction = 0;
	private int view_size = 200;
	private int life = 3;
	private boolean is_damaged = false;
	private int damageCurrent, damagedFrames = 5;
	private int anicurrent = 0, aniFrames = 120;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		ani = new BufferedImage[5];
		ani[0] = Game.spritesheet.getSprite((16*9), 0, 16, 16);//up
		ani[1] = Game.spritesheet.getSprite((16*8), 0, 16, 16);//down
		ani[2] = Game.spritesheet.getSprite(16, 16, 16, 16);//right
		ani[3] = Game.spritesheet.getSprite((16*2), 16, 16, 16);//left
		ani[4] = Game.spritesheet.getSprite(3*16, 16, 16, 16);//damage
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
		for(Enemy e : Game.enemies) {
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
	
	public void enemyDeath() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}
	
	public void collidingBullet() {
		for(BulletShoot e : Game.shots) {
			if(Entity.notPlayerCollided(e,this)) {
				life--;
				is_damaged = true;
				Game.shots.remove(e);
				return;
			}
		}
	}
	
	public void idleMoviment() {
		if(anicurrent > 60 && World.isFree(this.getX(), this.getY()+speed) && !isColliding(this.getX(), this.getY()+speed)) {
			this.setY(this.getY()+speed);
			this.direction = 1;
		}
		if(anicurrent < 60 && World.isFree(this.getX(), this.getY()-speed) && !isColliding(this.getX(), this.getY()-speed)) {
			this.setY(this.getY()-speed);
			this.direction = 0;
		}
		if(anicurrent > 70 && World.isFree(this.getX()+speed, this.getY()) && !isColliding(this.getX()+speed, this.getY())) {
			this.setX(this.getX()+speed);
			this.direction = 2;
		}
		if(anicurrent < 20 && World.isFree(this.getX()-speed, this.getY()) && !isColliding(this.getX()-speed, this.getY())) {
			this.setX(this.getX()-speed);
			this.direction = 3;
		}
	}
	
	public void tick() {
		if(!isCollidingPlayer()) {
			if(isSeeing()) {
				if(Game.rand.nextInt(100) < 70)
					dumbChase();
			}else {
					idleMoviment();
			}
		}else {
			if(Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(5);
				Game.player.gotDamage = true;
					if(Game.player.life <= 0) {
						//World.worldRestart(Game.level_now);
					}
			}
		}
		collidingBullet();
		if(this.life == 0)
			enemyDeath();
		if(is_damaged) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damagedFrames) {
				this.damageCurrent = 0;
				this.is_damaged = false;
			}
		}
		this.anicurrent++;
		if(this.anicurrent == this.aniFrames) {
			this.anicurrent = 0;
		}
		

	}
	
	public void render(Graphics g) {
		if(!is_damaged) {
			g.drawImage(ani[direction], this.getX() - Camera.x, this.getY() - Camera.y,World.tile_size,World.tile_size,null);
		}else {
			g.drawImage(ani[4], this.getX() - Camera.x, this.getY() - Camera.y,World.tile_size,World.tile_size,null);
		}
	}

}
