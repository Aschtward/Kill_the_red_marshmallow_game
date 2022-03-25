package com.omg.main;

import com.omg.world.World;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Menu {
	
	public String[] option = {"New Game","Continue","Load Game","Quit"};
	public int currentOption = 0;
	public int maxOption = option.length-1;
	public boolean up = false, down = false, enter;
	private BufferedImage icon;
	public static boolean pause = false;
	public static boolean saveExist = false;
	public static boolean saveGame = false;
	
	public Menu() {
		icon = Game.spritesheet.getSprite(32, 0, 16, 16);
	}
	
	public static void applySave(String str) {
		String[] apl = str.split("/");
		for(int i = 0; i < apl.length; i++) {
			String[] apl1 = apl[i].split(",");
			switch(apl1[0]) 
			{
				case "level":
					World.worldRestart("/map"+apl1[1]+".png");
					Game.gameState = "normal";
					pause = false;
					break;
				case "vida":
					Game.player.life = Integer.parseInt(apl1[1]);
					break;
				case "municao":
					Game.player.ammo = Integer.parseInt(apl1[1]);
					break;
				case "playerx":
					Game.player.setX(Integer.parseInt(apl1[1]));
					break;
				case "playery":
					Game.player.setY(Integer.parseInt(apl1[1]));
					break;
				case "gun":
					Game.player.setHasGun(true);
					break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader br = new BufferedReader(new FileReader("save.txt"));
				try {	
					while((singleLine = br.readLine()) != null) {
						String [] trans = singleLine.split(",");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line += trans[0];
						line +=",";
						line += trans[1];
						line += "/";
					}
				}catch(IOException e) {}
				
			}catch(FileNotFoundException e) {}
		}
		return line;
		
	}
	
	public static void saveGame(String[] val1, int[] val2,int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {}
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ",";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int j = 0 ; j < value.length ; j++) {
				value[j] += encode;
				current += value[j];
			}
			try {
				write.write(current);
				if(i < val1.length - 1) {
					write.newLine();
				}
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
	}
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExist = true;
		}else {
			saveExist = false;
		}
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
				file = new File("save.txt");
				file.delete();
				Game.gameState = "normal";
				World.worldRestart("/map1.png");
			}else if(option[currentOption] == "Load Game"){
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
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
