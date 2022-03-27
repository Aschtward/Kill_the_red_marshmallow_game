package com.omg.world;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

public class Light {
	
	private float x, y;
	private float radius;
	
	public Light(float x, float y, float radius) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public void render(Graphics2D g) {
		Point2D center = new Point2D.Float(x - Camera.x, y - Camera.y);
		float[] distance = {0.0f,1.0f};
		Color[] colors = {new Color(1.0f, 0.0f, 0.0f, 0.3f), Color.black};
		RadialGradientPaint p = new RadialGradientPaint(center,radius,distance,colors);
		g.setPaint(p);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .95F));
		g.fillRect(0,0,800,800);
		g.dispose();
	}

}
