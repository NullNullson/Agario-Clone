package managers;

import java.awt.Graphics;
import java.util.ArrayList;

import agario.CircularObject;
import agario.Dot;
import agario.Food;
import agario.Player;
import agario.PlayerCell;
import agario.Virus;
import physics.GameObject;

public class ClientGameObjectManager extends GameObjectManager{
	
	public void tick(){
		
		for(int i = 0; i < gameObjects.size(); i++){
			gameObjects.get(i).tick();
		}
		
	}
	
	public void removeGameObject(GameObject obj){
		
		gameObjects.remove(obj);
		
	}
	
	public void render(Graphics g, int xoffs, int yoffs, float zoom){
		for(int i = 0; i < gameObjects.size(); i++){
			gameObjects.get(i).render(g, xoffs, yoffs, zoom);
		}
	}
	
}
