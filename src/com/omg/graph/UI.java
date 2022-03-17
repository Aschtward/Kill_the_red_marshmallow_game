package com.omg.graph;

import java.awt.Color;
import java.awt.Graphics;

import com.omg.main.Game;
import com.omg.world.Camera;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(Game.player.getX()-8 - Camera.x, Game.player.getY()-8 - Camera.y,50, 6);
		g.setColor(Color.green);
		g.fillRect(Game.player.getX()-8 - Camera.x, Game.player.getY()-8 - Camera.y, (int)((Game.player.life/Game.player.maxlife)*50), 6);
		g.setColor(Color.white);
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxlife,Game.player.getX()-8 - Camera.x, Game.player.getY()-8-Camera.y);
	}
}
