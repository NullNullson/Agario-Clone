package managers;

import java.awt.Graphics;
import java.util.ArrayList;

import agario.Dot;
import agario.Food;
import agario.MassChangeListener;
import agario.MassiveGameObject;
import agario.Player;
import agario.PlayerCell;
import agario.PositionChangeListener;
import agario.Virus;
import network.ClientGameObjectNotifier;
import physics.GameObject;

/**
 * GameObjectManager for use on the server-side. Includes stuff
 * like a queue for notifying the client of new gameobjects.
 * @author Troy
 *
 */
public class ServerGameObjectManager extends GameObjectManager implements PositionChangeListener, MassChangeListener{
	
	private ArrayList<GameObject> createdQueue;
	
	private ArrayList<GameObject> movedQueue;
	
	private ArrayList<MassiveGameObject> massChangedQueue;
	
	private ArrayList<GameObject> removedQueue;
	
	private ClientGameObjectNotifier notifier;
	
	public ServerGameObjectManager(ClientGameObjectNotifier notifier){
		
		this.notifier = notifier;
		
		createdQueue = new ArrayList<GameObject>();
		
		movedQueue = new ArrayList<GameObject>();
		
		massChangedQueue = new ArrayList<MassiveGameObject>();
		
		removedQueue = new ArrayList<GameObject>();
		
	}
	
	public void tick(){
		
		for(int i = 0; i < gameObjects.size(); i++){
			
			gameObjects.get(i).tick();
			
		}
		
		for(int i = 0; i < createdQueue.size(); i++){
			
			notifier.newGameObject(createdQueue.get(i));
			
		}
		
		for(int i = 0; i < movedQueue.size(); i++){
			
			notifier.updatePosition(movedQueue.get(i));
			
		}
		
		for(int i = 0; i < massChangedQueue.size(); i++){
			
			notifier.updateMass(massChangedQueue.get(i));
			
		}
		
		for(int i = 0; i < removedQueue.size(); i++){
			
			notifier.removeGameObject(removedQueue.get(i));
			
		}
		
		createdQueue.clear();
		movedQueue.clear();
		massChangedQueue.clear();
		removedQueue.clear();
		
		ArrayList<GameObject> toRemove = new ArrayList<GameObject>();
		
		for(int i = 0; i < gameObjects.size(); i++){
			for(int i2 = 0; i2 < gameObjects.size(); i2++){
				if(i != i2){
					
					GameObject obj1 = gameObjects.get(i);
					GameObject obj2 = gameObjects.get(i2);
					
					if(obj1 instanceof Player){
						
						if(obj2 instanceof Dot){
							
							Player player = (Player)obj1;
							
							Dot dot = (Dot)obj2;
							
							PlayerCell intersected = null;
							
							if((intersected = player.intersects(dot)) != null){
								
								toRemove.add(obj2);
								
								intersected.setMass(intersected.getMass() + dot.getMass());
								
							}
							
						}
						else if(obj2 instanceof Food){
							
							Player player = (Player)obj1;
							
							Food food = (Food)obj2;
							
							PlayerCell intersected = null;
							
							if((intersected = player.intersects(food)) != null){
								
								toRemove.add(obj2);
								
								intersected.setMass(intersected.getMass() + food.getMass());
								
							}
							
						}
						else if(obj2 instanceof Virus){

							Player player = (Player)obj1;
							
							Virus virus = (Virus)obj2;
							
							PlayerCell intersected = null;
							
							if((intersected = player.intersects(virus)) != null){
								
								toRemove.add(obj2);
								
								intersected.setMass(intersected.getMass() + virus.getMass());
								
								player.splitCell(intersected, 10);
								
							}
							
						}
						else if(obj2 instanceof Player){
							
							Player player = (Player)obj1;
							
							Player player2 = (Player)obj2;
							
							PlayerCell[] intersection = null;
							
							if((intersection = player.intersects(player2)) != null){
								
								if(intersection[0].getMass() > intersection[1].getMass()){
									
									if(intersection[1].getMass() / intersection[0].getMass() <= Player.EATING_THESHOLD_PERCENTAGE){
										
										player2.removeCell(intersection[1]);
										
										intersection[0].setMass(intersection[0].getMass() + intersection[1].getMass());
										
									}
									
								}
								else{
									
									if(intersection[0].getMass() / intersection[1].getMass() <= Player.EATING_THESHOLD_PERCENTAGE){
										
										player.removeCell(intersection[0]);
										
										intersection[1].setMass(intersection[1].getMass() + intersection[0].getMass());
										
									}
									
								}
								
							}
							
						}
						
					}
					else if(obj1 instanceof Virus && obj2 instanceof Food){
						
						Virus virus = (Virus)obj1;
						
						Food food = (Food)obj2;
						
						if(virus.intersects(food)){
							
							virus.eatFood(food, this);
							
							toRemove.add(food);
							
						}
						
					}
					
				}
			}
		}
		
		for(int i = 0; i < toRemove.size(); i++){
			
			removeGameObject(toRemove.get(i));
			
		}
		
	}
	
	public void render(Graphics g, int xoffs, int yoffs, float zoom){
		
		for(int i = 0; i < gameObjects.size(); i++){
			
			gameObjects.get(i).render(g, xoffs, yoffs, zoom);
		
		}
		
	}
	
	public void removeGameObject(GameObject obj){
		
		if(gameObjects.contains(obj)){
			
			gameObjects.remove(obj);
			
		}
		
		notifyOfRemoval(obj);
		
	}
	
	public void notifyOfRemoval(GameObject obj){
		
		removedQueue.add(obj);
		
	}
	
	public void notifyOfCreation(GameObject obj){
		
		createdQueue.add(obj);
		
		obj.addPositionChangeListener(this);
		
		if(obj instanceof MassiveGameObject){
			
			((MassiveGameObject)obj).addMassChangeListener(this);
			
		}
		
	}
	
	public void onPositionChange(GameObject obj){
		
		movedQueue.add(obj);
		
	}
	
	public void onMassChange(MassiveGameObject obj) {
		
		massChangedQueue.add(obj);
		
	}
	
}
