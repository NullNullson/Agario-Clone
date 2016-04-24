package gamestate;

import java.awt.Graphics;

import main.Main;
import managers.ClientGameObjectManager;
import managers.GameObjectManager;

public abstract class MainGameState extends GameState{
	
	protected Main main;
	
	protected GameObjectManager manager;
	
	protected int xoffs = 0, yoffs = 0;
	
	protected MainGameState(Main main){
		
		this.main = main;
		
	}
	
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
