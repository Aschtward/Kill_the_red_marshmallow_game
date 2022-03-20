package com.omg.main;


import com.omg.world.World;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Menu {
	
	public String[] option = {"New Game","Continue","Load Game","Quit"};
	public int currentOption = 0;
	public int maxOption = option.length-1;
	public boolean up = false, down = false, enter;
	private BufferedImage icon;
	
	public Menu() {
		icon = Game.spritesheet.getSprite(32, 0, 16, 16);
	}
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}if(enter) {
			enter = false;
			if(option[currentOption] == "New Game") {
				Game.gameState = "normal";
				World.worldRestart("/map.png");
			}else if(option[currentOption] == "Continue") {
				Game.gameState = "normal";
			}else if(option[currentOption] == "Quit") {
				System.exit(1);
			}
		}
	}
	public void render(Graphics g) {
		
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("Kill the red Marshmallow!",(Game.WIDTH*Game.SCALE)/4 - 45,(Game.HEIGHT*Game.SCALE)/2 - 20);
		
		g.drawImage(icon,(Game.WIDTH*Game.SCALE - 70)/2,(Game.HEIGHT*Game.SCALE)/7,64,64,null);
		
		g.setFont(new Font("arial",Font.BOLD,12));
		g.drawString("New Game",(Game.WIDTH*Game.SCALE)/4 + 39,(Game.HEIGHT*Game.SCALE)/2 + 10);
		g.drawString("Continue",(Game.WIDTH*Game.SCALE)/4 + 42,(Game.HEIGHT*Game.SCALE)/2 + 30);
		g.drawString("Load Game",(Game.WIDTH*Game.SCALE)/4 + 38,(Game.HEIGHT*Game.SCALE)/2 + 50);
		g.drawString("Quit",(Game.WIDTH*Game.SCALE)/4 + 55,(Game.HEIGHT*Game.SCALE)/2 + 70);
		
		if(option[currentOption] == "New Game") {
			g.drawString(">",(Game.WIDTH*Game.SCALE)/4 + 25,(Game.HEIGHT*Game.SCALE)/2 + 10);
		}
		if(option[currentOption] == "Continue") {
			g.drawString(">",(Game.WIDTH*Game.SCALE)/4 + 25,(Game.HEIGHT*Game.SCALE)/2 + 30);
		}
		if(option[currentOption] == "Load Game") {
			g.drawString(">",(Game.WIDTH*Game.SCALE)/4 + 25,(Game.HEIGHT*Game.SCALE)/2 + 50);
		}
		if(option[currentOption] == "Quit") {
			g.drawString(">",(Game.WIDTH*Game.SCALE)/4 + 25,(Game.HEIGHT*Game.SCALE)/2 + 70);
		}
	}
}
