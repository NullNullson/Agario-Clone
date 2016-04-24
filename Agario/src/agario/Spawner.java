package agario;

import java.awt.Color;
import java.util.ArrayList;

import managers.ClientGameObjectManager;
import managers.GameObjectManager;
import managers.ServerGameObjectManager;
import physics.GameObject;
import physics.Vector;

public class Spawner {
	
	private int maxSpawnInterval;
	
	private int minSpawnInterval;
	
	private GameObjectManager manager;
	
	private long nextSpawnTime = 0;
	
	public Spawner(GameObjectManager manager, int maxSpawnInterval, int minSpawnInterval){
		
		this.manager = manager;
		
		this.maxSpawnInterval = maxSpawnInterval;
		
		this.minSpawnInterval = minSpawnInterval;
		
	}
	
	private Color randColor(){
		
		int r = (int)(Math.random() * 255);
		int g = (int)(Math.random() * 255);
		int b = (int)(Math.random() * 255);
		
		return new Color(r, g, b);
		
	}
	
	public void tick(){
		
		if(System.currentTimeMillis() >= nextSpawnTime){
			
			GameObject obj = null;
			
			if((int)(Math.random() * 20) <= 17){
				
				if(manager instanceof ServerGameObjectManager){
					
					obj = new Dot(new Vector((int)(Math.random() * ServerGame.WORLD_WIDTH), (int)(Math.random() * ServerGame.WORLD_HEIGHT)), randColor());
					
					obj.setId(ID.newId((ServerGameObjectManager)manager, obj));
					
					manager.addGameObject(obj);
					
				}
				
			}
			else{
				
				int xpos = (int)(Math.random() * ServerGame.WORLD_WIDTH);
				int ypos = (int)(Math.random() * ServerGame.WORLD_HEIGHT);
				
				ArrayList<GameObject> gameObjects = manager.getGameObjects();
				
				if(manager instanceof ServerGameObjectManager){
					
					obj = new Virus(new Vector(xpos, ypos));
					
					boolean intersected = false;
					
					for(int i = 0; i < gameObjects.size(); i++){
						
						if(gameObjects.get(i) instanceof Player){
							
							Player player = (Player)gameObjects.get(i);
							
							if(player.intersects((Virus)obj) != null){
								
								intersected = true;
								
								break;
								
							}
							
						}
						
					}
					
					if(!intersected){
						
						obj.setId(ID.newId((ServerGameObjectManager)manager, obj));
						
						manager.addGameObject(obj);
						
					}
					
				}
				
			}
			
			nextSpawnTime = System.currentTimeMillis() + (int)(Math.random() * (maxSpawnInterval - minSpawnInterval)) + minSpawnInterval;
			
		}
		
	}
	
}
