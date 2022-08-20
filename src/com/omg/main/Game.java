package com.omg.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.omg.entities.BulletShoot;
import com.omg.entities.Enemy;
import com.omg.entities.Entity;
import com.omg.entities.Player;
import com.omg.graph.Spritesheet;
import com.omg.graph.UI;
import com.omg.world.World;

public class Game extends Canvas implements Runnable, KeyListener,MouseListener{
	
	
	private static final long serialVersionUID = 1227254042505466843L;
	
	///Definindo parametros para janela
	public static JFrame frame;
	public static int WIDTH = 300;
	public static int HEIGHT = 300;
	public static int SCALE = 1;
	///Fim parametros para janela
	
	public static BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> shots;
	public static Spritesheet spritesheet;
	public static Player player;
	public static Random rand;
	public static UI ui;
	public String level_now = "/map.png";
	public static String gameState = "menu";
	private int level_number = 1;
	private int level_max = 2;
	public static World world;
	public Menu menu;
	private Thread thread;
	private boolean isRunning = true;
	public boolean restartGame = false;
	public boolean saveGame = false;
	public static int[] pixels;



	
	public Game() {
		
		///Cria��o da Janela
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		inicia_frame();
		///
		addKeyListener(this);
		addMouseListener(this);
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		shots = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/text.png");
		player = new Player(0,0,0,0, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/map1.png");
		rand = new Random();
		ui = new UI();
		menu = new Menu();

		
	}
	
	public void inicia_frame() {///Inicializa janela
		frame = new JFrame("Kill the red marshmallow");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
		
	public void tick() {
		if(gameState == "normal") {
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level","vida","municao","playerx","playery","gun"};
				int[] op2 = {level_number,(int)player.life,player.ammo,player.getX(),player.getY(),player.gotGun()};
				Menu.saveGame(opt1, op2, 10);
			}
			for(int i = 0; i < entities.size();i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for(int i = 0; i<  shots.size() ;i++) {
				shots.get(i).tick();
			}
			if(enemies.isEmpty()) {
				//proxima fase
				if(level_number < level_max) {
					level_number++;
					level_now = "/map" + level_number + ".png";
					World.worldRestart(level_now);
				}	
			}
		}else if(gameState == "menu") {
			menu.tick();
		}
	}
	
	public void  render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g = bs.getDrawGraphics();
		world.render(g);
			if(gameState == "normal") {
				this.restartGame = false;
				for(int i = 0; i < entities.size();i++) {
					Entity e = entities.get(i);
					e.render(g);
				}
				for(int i = 0; i<  shots.size() ;i++) {
					shots.get(i).render(g);
				}

				ui.render(g);
				g.setFont(new Font("arial",Font.BOLD,17));
				g.drawString("Muni��o: " + player.ammo,10,20);
			}
			if(gameState == "game_over") {
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0,0,0,100));
				g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
				g2.setFont(new Font("arial", Font.BOLD,28));
				g2.setColor(Color.LIGHT_GRAY);
				g.drawString("GAME OVER", ((WIDTH*SCALE)/4), ((HEIGHT*SCALE)/2));
				g2.setFont(new Font("arial", Font.BOLD,18));
				g.drawString("Enter to try again", ((WIDTH*SCALE)/4) + 10, ((HEIGHT*SCALE)/2) + 20);
				
				if(restartGame) {
					
					this.restartGame = false;
					Game.gameState = "normal";
					World.worldRestart(level_now);				
			}
		}else if(gameState == "menu") {
			menu.render(g);
		}
		
		bs.show();
	}
	
	public void run() {
		
		//Implementa��o game looping
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		int frames = 0;
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta+=(now - lastTime)/ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				frames = 0;
				timer += 1000;
			}
		}//Fim implementa��o game looping
		
		stop();
}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;	
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_E) {
			player.shooting = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "menu") {
				menu.enter = true;
			}
		}
		if(Game.gameState == "menu") {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				menu.up = true;
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				menu.down = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "menu";
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(gameState == "normal")
				this.saveGame = true;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX()/SCALE);
		player.my = (e.getY()/SCALE);
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
