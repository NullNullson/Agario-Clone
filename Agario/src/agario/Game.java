package agario;

import java.awt.Graphics;

import main.Main;
import managers.ClientGameObjectManager;
import managers.GameObjectManager;

public abstract class Game {
	
	protected Main main;
	
	protected GameObjectManager manager;
	
	protected int xoffs = 0, yoffs = 0;
	
	protected Game(Main main){
		
		this.main = main;
		
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public Main getMain(){
		
		return main;
		
	}
	
	public int getXOffs(){
		
		return xoffs;
		
	}
	
	public int getYOffs(){
		
		return yoffs;
		
	}
	
	public GameObjectManager getGameObjectManager(){
		
		return manager;
		
	}
	
}
