package network;

import java.awt.Color;
import java.util.ArrayList;

import agario.ClientPlayerCell;
import agario.Dot;
import agario.Food;
import agario.GameMode;
import agario.MassiveGameObject;
import agario.Player;
import agario.PlayerCell;
import agario.Virus;
import gamestate.ClientGame;
import gamestate.MainGameState;
import gamestate.MenuState;
import gamestate.ServerGame;
import main.Main;
import managers.ClientGameObjectManager;
import managers.GameObjectManager;
import managers.ServerGameObjectManager;
import physics.GameObject;
import physics.Vector;

/**
 * Takes instructions from the server/client and interprets them.
 * @author Troy
 *
 */
public class AgarioParser {
	
	private ArrayList<String> queue = new ArrayList<String>();
	
	private GameObjectManager manager;
	
	private GameMode gameMode;
	
	private MainGameState game;
	
	private Main main;
	
	public AgarioParser(Main main, GameObjectManager manager, MainGameState game, GameMode gameMode){
		
		this.main = main;
		
		this.game = game;
		
		this.manager = manager;
		
		this.gameMode = gameMode;
		
	}
	
	public void tick(){
		
		synchronized(queue){
			
			if(gameMode == GameMode.CLIENT){
				
				if(queue.size() > 0){
					
					for(int i = 0; i < queue.size(); i++){
						
						String command = queue.get(i);
						
						if(command != null){
							
							String[] parts = command.split(",");
							
							if(parts[0].equals("m")){
								
								int id = Integer.parseInt(parts[1]);
								
								float x = Float.parseFloat(parts[2]);
								
								float y = Float.parseFloat(parts[3]);
								
								GameObject withId = null;
								
								if((withId = manager.getGameObjectById(id)) != null){
									
									withId.setPosition(new Vector(x, y));
									
								}
								
							}
							else if(parts[0].equals("c")){
								
								String type = parts[1];
								
								if(type.equals("pc")){
									
									int id = Integer.parseInt(parts[2]);
									
									float x = Float.parseFloat(parts[3]);
									
									float y = Float.parseFloat(parts[4]);
									
									float mass = Float.parseFloat(parts[5]);
									
									int r = Integer.parseInt(parts[6]);
									
									int g = Integer.parseInt(parts[7]);
									
									int b = Integer.parseInt(parts[8]);
									
									Color color = new Color(r, g, b);
									
									String name = parts[9];
									
									ClientPlayerCell cell = new ClientPlayerCell(id, new Vector(x, y), new Vector(0, 0), color, name, mass);
									
									manager.addGameObject(cell);
									
								}
								else if(type.equals("d")){
									
									int id = Integer.parseInt(parts[2]);
									
									float x = Float.parseFloat(parts[3]);
									
									float y = Float.parseFloat(parts[4]);
									
									float mass = Float.parseFloat(parts[5]);
									
									int r = Integer.parseInt(parts[6]);
									
									int g = Integer.parseInt(parts[7]);
									
									int b = Integer.parseInt(parts[8]);
									
									Color color = new Color(r, g, b);
									
									Dot dot = new Dot(id, new Vector(x, y), new Vector(0, 0), color);
									
									manager.addGameObject(dot);
									
								}
								else if(type.equals("v")){
									
									int id = Integer.parseInt(parts[2]);
									
									float x = Float.parseFloat(parts[3]);
									
									float y = Float.parseFloat(parts[4]);
									
									float mass = Float.parseFloat(parts[5]);
									
									int r = Integer.parseInt(parts[6]);
									
									int g = Integer.parseInt(parts[7]);
									
									int b = Integer.parseInt(parts[8]);
									
									Color color = new Color(r, g, b);
									
									Virus virus = new Virus(id, new Vector(x, y), new Vector(0, 0));
									
									manager.addGameObject(virus);
									
								}
								else if(type.equals("f")){
									
									int id = Integer.parseInt(parts[2]);
									
									float x = Float.parseFloat(parts[3]);
									
									float y = Float.parseFloat(parts[4]);
									
									float mass = Float.parseFloat(parts[5]);
									
									int r = Integer.parseInt(parts[6]);
									
									int g = Integer.parseInt(parts[7]);
									
									int b = Integer.parseInt(parts[8]);
									
									Color color = new Color(r, g, b);
									
									Food food = new Food(id, new Vector(x, y), new Vector(0, 0), color);
									
									manager.addGameObject(food);
									
								}
								
							}
							else if(parts[0].equals("r")){
								
								int id = Integer.parseInt(parts[1]);
								
								GameObject withId = null;
								
								if((withId = manager.getGameObjectById(id)) != null){
									
									((ClientGameObjectManager)manager).removeGameObject(withId);
									
								}
								
							}
							else if(parts[0].equals("offs")){
								
								int xoffs = Integer.parseInt(parts[1]);
								
								int yoffs = Integer.parseInt(parts[2]);
								
								((ClientGame)game).setOffset(xoffs, yoffs);
								
							}
							else if(parts[0].equals("ma")){
								
								int id = Integer.parseInt(parts[1]);
								
								float mass = Float.parseFloat(parts[2]);
								
								GameObject withId = game.getGameObjectManager().getGameObjectById(id);
								
								if(withId instanceof MassiveGameObject){
									
									MassiveGameObject obj = (MassiveGameObject)withId;
									
									obj.setMass(mass);
									
								}
								
							}
							else if(parts[0].equals("d")){
								
								main.setState(new MenuState(main));
								
							}
							
						}
						
					}
					
					queue.clear();
					
				}
				
			}
			else if(gameMode == GameMode.SERVER){
				
				if(queue.size() > 0){
					
					for(int i = 0; i < queue.size(); i++){
						
						String command = queue.get(i);
						
						if(command != null){
							
							String[] parts = command.split(",");
							
							if(parts.length == 2){
								
								int x = Integer.parseInt(parts[0]);
								int y = Integer.parseInt(parts[1]);
								
								int id = ((ServerGame)game).getServer().getClientId();
								
								GameObject withId = manager.getGameObjectById(id);
								
								if(withId instanceof Player){
									
									Player player = (Player)withId;
									
									Vector playerPos = player.getPosition();
									
									int xoffs = -(int)playerPos.x + Main.WIDTH / 2;
									int yoffs = -(int)playerPos.y + Main.HEIGHT / 2;
									
									player.setMousePosition(x, y, xoffs, yoffs);
									
									((ServerGame)game).getServer().sendInfo("offs," + xoffs + "," + yoffs + "\n");
									
								}
								
							}
							else if(parts.length == 1){
								
								if(parts[0].equals("handshake")){
									
									int id = ((ServerGame)game).getServer().getClientId();
									
									Player player = new Player(id, game, new Vector(0, 0), Color.blue, "Player 2");
									
									player.addCellChangeListener((ServerGameObjectManager)manager);
									
									manager.addGameObject(player);
									
								}
								else if(parts[0].equals("w")){
									
									int id = ((ServerGame)game).getServer().getClientId();
									
									GameObject withId = manager.getGameObjectById(id);
									
									if(withId instanceof Player){
										
										Player player = (Player)withId;
										
										player.launchFood();
										
									}
									
								}
								else if(parts[0].equals("space")){
									
									int id = ((ServerGame)game).getServer().getClientId();
									
									GameObject withId = manager.getGameObjectById(id);
									
									if(withId instanceof Player){
										
										Player player = (Player)withId;
										
										player.split();
										
									}
									
								}
								
							}
							
						}
						
					}
					
					queue.clear();
					
				}
				
			}
			
		}
		
	}
	
	public void addToQueue(String info){
		
		queue.add(info);
		
	}
	
}
