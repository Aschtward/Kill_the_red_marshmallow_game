package com.omg.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.omg.entities.Enemy;
import com.omg.entities.Entity;
import com.omg.entities.Player;
import com.omg.graph.Spritesheet;
import com.omg.graph.UI;
import com.omg.world.World;

public class Game extends Canvas implements Runnable, KeyListener{
	
	
	private static final long serialVersionUID = 1227254042505466843L;
	
	///Definindo parametros para janela
	public static JFrame frame;
	public static int WIDTH = 400;
	public static int HEIGHT = 400;
	public static int SCALE = 1;
	///Fim parametros para janela
	
	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static Spritesheet spritesheet;
	public static Player player;
	public static Random rand;
	public UI ui;
	
	public static World world;
	private Thread thread;
	private boolean isRunning = true;

	
	public Game() {
		
		///Criação da Janela
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		inicia_frame();
		///
		addKeyListener(this);
		
		
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		spritesheet = new Spritesheet("/text.png");
		player = new Player(0,0,0,0, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/map.png");
		rand = new Random();
		ui = new UI();
		
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
		for(int i = 0; i < entities.size();i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}
	
	public void  render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		///Implementação fundo  preto
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,WIDTH,HEIGHT);
		g = bs.getDrawGraphics();
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		world.render(g);
		for(int i = 0; i < entities.size();i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		ui.render(g);
		g.setFont(new Font("arial",Font.BOLD,17));
		g.drawString("Munição: " + player.ammo,10,20);
		bs.show();
	}
	
	public void run() {
		
		//Implementação game looping
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
		}//Fim implementação game looping
		
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


}
