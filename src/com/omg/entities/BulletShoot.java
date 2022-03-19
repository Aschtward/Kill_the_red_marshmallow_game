package com.omg.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.omg.main.Game;
import com.omg.world.Camera;


public class BulletShoot extends Entity {
	
	private double dx,dy;
	private double spd = 5;
	private int curllife = 0;
	private int life = 60;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
		// TODO Auto-generated constructor stub
	}
	
	public void tick() {
		setX(getX()+(int)(dx*spd));
		setY(getY()+(int)(dy*spd));
		curllife++;
		if(curllife == life) {
			Game.shots.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, getWidth(), getHeight());
	}
}
