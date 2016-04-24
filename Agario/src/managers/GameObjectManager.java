package managers;

import java.awt.Graphics;
import java.util.ArrayList;

import physics.GameObject;

public abstract class GameObjectManager {
	
	protected ArrayList<GameObject> gameObjects;
	
	public GameObjectManager(){
		
		gameObjects = new ArrayList<GameObject>();
		
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g, int xoffs, int yoffs, float zoom);
	
	public void addGameObject(GameObject obj){
		
		gameObjects.add(obj);
		
	}
	
	public GameObject getGameObjectById(int id){
		
		for(int i = 0; i < gameObjects.size(); i++){
			
			if(gameObjects.get(i).getId() == id){
				
				return gameObjects.get(i);
				
			}
			
		}
		
		return null;
		
	}
	
	public ArrayList<GameObject> getGameObjects(){
		
		return gameObjects;
		
	}
	
}
