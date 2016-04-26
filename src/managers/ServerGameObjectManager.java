package managers;

import java.awt.Graphics;
import java.util.ArrayList;

import agario.CellChangeListener;
import agario.Dot;
import agario.Food;
import agario.ID;
import agario.MassChangeListener;
import agario.MassiveGameObject;
import agario.Player;
import agario.PlayerCell;
import agario.PositionChangeListener;
import agario.Virus;
import agario.VirusBomb;
import gamestate.ServerGame;
import network.ClientGameObjectNotifier;
import physics.GameObject;
import physics.Vector;

/**
 * GameObjectManager for use on the server-side. Includes stuff
 * like a queue for notifying the client of new gameobjects.
 * @author Troy
 *
 */
public class ServerGameObjectManager extends GameObjectManager implements PositionChangeListener, MassChangeListener, CellChangeListener{
	
	private ArrayList<GameObject> createdQueue;
	
	private ArrayList<GameObject> movedQueue;
	
	private ArrayList<MassiveGameObject> massChangedQueue;
	
	private ArrayList<GameObject> removedQueue;
	
	private ClientGameObjectNotifier notifier;
	
	private ServerGame game;
	
	public ServerGameObjectManager(ServerGame game, ClientGameObjectNotifier notifier){
		
		this.game = game;
		
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
		ArrayList<GameObject> toAdd = new ArrayList<GameObject>();
		
		for(int i = 0; i < gameObjects.size(); i++){
			
			GameObject obj1 = gameObjects.get(i);
			
			if(obj1 instanceof VirusBomb){
				
				VirusBomb bomb = (VirusBomb)obj1;
				
				if(bomb.readyToDetonate()){
					
					toRemove.add(bomb);
					
					for(int x = 0; x < VirusBomb.NUM_VIRUSES; x++){
						
						Virus virus = new Virus(bomb.getPosition(), Vector.random().mult((float)(Math.random() * (VirusBomb.MAX_LAUNCH_SPEED - VirusBomb.MIN_LAUNCH_SPEED) + VirusBomb.MIN_LAUNCH_SPEED)));
						
						int id = ID.newId(this, virus);
						
						virus.setId(id);
						
						toAdd.add(virus);
						
					}
					
				}
				
			}
			
			for(int i2 = 0; i2 < gameObjects.size(); i2++){
				if(i != i2){
					
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
		
		for(int i = 0; i < toAdd.size(); i++){
			
			addGameObject(toAdd.get(i));
			
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
		
		initialize(obj);
		
	}
	
	public void initialize(GameObject obj){
		
		obj.addPositionChangeListener(this);
		
		if(obj instanceof MassiveGameObject){
			
			((MassiveGameObject)obj).addMassChangeListener(this);
			
		}
		
		if(obj instanceof Player){
			
			((Player)obj).addCellChangeListener(this);
			
		}
		
	}
	
	public void onPositionChange(GameObject obj){
		
		movedQueue.add(obj);
		
	}
	
	public void onMassChange(MassiveGameObject obj) {
		
		massChangedQueue.add(obj);
		
	}
	
	public void onCellChange(Player player){
		
		if(player.getCells().size() == 0){
			
			if(player.getId() == game.getServer().getClientId()){
				
				notifier.playerDied();
				
			}
			
		}
		
	}
	
}
