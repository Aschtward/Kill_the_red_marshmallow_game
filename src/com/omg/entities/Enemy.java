package com.omg.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.omg.main.Game;
import com.omg.world.AStar;
import com.omg.world.Camera;
import com.omg.world.Node;
import com.omg.world.Vector2i;
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
		Rectangle enemy = new Rectangle(xnext,ynext,World.tile_size - 10,World.tile_size - 10);
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
		if(this.distanceBetwen(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < this.view_size) {
			return true;
		}
		return false;
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
	
	public  void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size()-1).tile;
				if(this.getX() <  target.x*World.tile_size && !isColliding(this.getX()+speed,this.getY())) {
					this.setX(this.getX()+speed);
					this.direction = 2;
				}else if(this.getX() > target.x*World.tile_size && !isColliding(this.getX()-speed,this.getY())) {
					this.setX(this.getX()-speed);
					this.direction = 3;
					this.direction = 1;
				}
				if(this.getY() < target.y*World.tile_size && !isColliding(this.getX(),this.getY()+speed)) {
					this.setY(this.getY()+speed);
				}else if(this.getY() > target.y*World.tile_size && !isColliding(this.getX(),this.getY()-speed)) {
					this.setY(this.getY()-speed);
					this.direction = 0;
				}
				if(this.getX() == target.x*World.tile_size && this.getY() == target.y*World.tile_size) {
					path.remove(path.size()-1);
				}
			}
		}
	}
	
	public void tick() {
		if(!isCollidingPlayer()) {
			if(isSeeing()) {
				if(path == null || path.size() == 0) {
					Vector2i start = new Vector2i((int)(this.getX()/World.tile_size),(int)(this.getY()/World.tile_size));
					Vector2i end = new Vector2i((int)(Game.player.getX()/World.tile_size),(int)(Game.player.getY()/World.tile_size));
					path =  AStar.findPath(Game.world, start, end);
				}
				if(Game.rand.nextInt(100) < 90)
					followPath(path);
			}else {
					idleMoviment();
			}
		}else {
			if(Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(5);
				Game.player.gotDamage = true;
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
