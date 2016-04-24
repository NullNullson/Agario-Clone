package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import agario.GameMode;
import gamestate.ClientGame;
import gamestate.GameState;
import gamestate.MainGameState;
import gamestate.MenuState;
import gamestate.ServerGame;

public class Main extends Canvas implements Runnable{
	
	private Thread thread;
	private boolean running;
	
	private Graphics g;
	
	private BufferStrategy bs = null;
	
	private GameState state;
	
	public static final int WIDTH = 2000, HEIGHT = 1400;
	
	public Main(){
		
		thread = new Thread(this);
		running = true;
		
		setBackground(Color.white);
		
		state = new MenuState(this);
		
	}
	
	public void paint(Graphics g){
		if(bs == null){
			this.createBufferStrategy(2);
			bs = this.getBufferStrategy();
			this.g = bs.getDrawGraphics();
			thread.start();
		}
	}
	
	private void tick(){
		
		state.tick();
		
	}
	
	private void draw(){
		
		g.clearRect(0, 0, getWidth(), getHeight());
		
		state.render(g);
		
	}
	
	private void drawToScreen(){
		bs.show();
	}
	
	public void run(){
		
		while(running){
			
			tick();
			
			draw();
			
			drawToScreen();
			
			try{
				
				Thread.sleep(10);
				
			}
			catch(InterruptedException e){
				
				e.printStackTrace();
				
			}
			
		}
		
	}
	
	public void setState(GameState state){
		
		this.state.destroy();
		
		this.state = state;
		
	}
	
}
