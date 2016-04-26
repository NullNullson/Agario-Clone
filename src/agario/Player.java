package agario;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import org.omg.CORBA.Current;

import gamestate.MainGameState;
import managers.GameObjectManager;
import managers.ServerGameObjectManager;
import physics.GameObject;
import physics.Vector;

public class Player extends GameObject{
	
	private Color color;
	
	private String name;
	
	private Vector mousePos = new Vector(500, 500);
	
	// The maximum ratio of one cell's mass to another in order for the smaller one to be eaten
	public static final float EATING_THESHOLD_PERCENTAGE = 0.8f;
	
	public static final int MAX_CELLS = 16;
	
	private ArrayList<PlayerCell> cells = new ArrayList<PlayerCell>();
	
	private int numCells;
	
	private int prevNumCells;
	
	private MainGameState game;
	
	private CellChangeListener cellChangeListener;
	
	public Player(int id, MainGameState game, Vector position, Color color, String name){
		
		super(position, id);
		
		this.color = color;
		
		this.name = name;
		
		this.game = game;
		
		if(game.getGameObjectManager() instanceof ServerGameObjectManager){
			
			PlayerCell mainCell = new PlayerCell(this, position, new Vector(0, 0), color, name, 128);
		
			mainCell.setId(ID.newId((ServerGameObjectManager)game.getGameObjectManager(), mainCell));
			
			cells.add(mainCell);
			
		}
		
		numCells = cells.size();
		
		prevNumCells = numCells;
		
	}
	
	public Player(MainGameState game, Vector position, Color color, String name){
		
		this(0, game, position, color, name);
		
	}
	
	public void tick(){
		
		Vector netPosition = new Vector(0, 0);
		
		int netPositionDividend = 0;
		
		for(int i = 0; i < cells.size(); i++){
			
			PlayerCell current = cells.get(i);
			
			netPosition = netPosition.add(current.getPosition().mult(current.getMass()));
			
			netPositionDividend += current.getMass();
			
			current.setMousePosition(mousePos);
			
			current.tick();
			
		}
		
		ArrayList<PlayerCell> toRemove = new ArrayList<PlayerCell>();
		ArrayList<PlayerCell> toAdd = new ArrayList<PlayerCell>();
		
		for(int i = 0; i < cells.size(); i++){
			
			for(int i2 = 0; i2 < cells.size(); i2++){
				
				if(i != i2){
					
					PlayerCell cell1 = cells.get(i);
					PlayerCell cell2 = cells.get(i2);
					
					if(cell1.intersects(cell2)){
						
						if(cell1.isReadyToCombine() && cell2.isReadyToCombine() && i2 >= i){
							
							//if(cell1.withinEatingProximity(cell2)){
								
								cell1.setReadyToCombine(false);
								cell2.setReadyToCombine(false);
								
								toRemove.add(cell1);
								toRemove.add(cell2);
								
								if(game.getGameObjectManager() instanceof ServerGameObjectManager){
									
									PlayerCell cell = new PlayerCell(this, cell1.getPosition().add(cell2.getPosition()).div(2), new Vector(0, 0), color, name, cell1.getMass() + cell2.getMass());
									
									cell.setId(ID.newId((ServerGameObjectManager)game.getGameObjectManager(), cell));
									
									toAdd.add(cell);
									
								}
								
							//}
							
						}
						else{
							
							Vector pos1 = cell1.getPosition();
							Vector pos2 = cell2.getPosition();
							
							Vector between = pos1.sub(pos2);
							
							float targetDist = cell1.getRadius() + cell2.getRadius();
							
							float actualDist = between.magnitude();
							
							float extensionFactor = targetDist - actualDist;
							
							if(between.magnitude() != 0){
								
								cell1.setPosition(cell1.getPosition().add(between.normalized().mult(extensionFactor)));
								
							}
							else{
								
								cell1.setPosition(cell1.getPosition().add(Vector.random().normalized().mult(extensionFactor)));
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		cells.removeAll(toRemove);
		cells.addAll(toAdd);
		
		GameObjectManager manager = game.getGameObjectManager();
		
		if(manager instanceof ServerGameObjectManager){
			
			ServerGameObjectManager theManager = (ServerGameObjectManager)manager;
			
			for(int i = 0; i < toRemove.size(); i++){
				
				theManager.removeGameObject(toRemove.get(i));
				
			}
			
		}
		
		netPosition = netPosition.div(netPositionDividend);
		
		setPosition(netPosition);
		
		prevNumCells = numCells;
		
		numCells = cells.size();
		
		if(prevNumCells != numCells){
			
			if(cellChangeListener != null){
				
				cellChangeListener.onCellChange(this);
				
			}
			
		}
		
	}
	
	public void render(Graphics g, int xoffs, int yoffs, float zoom){
		
		for(int i = 0; i < cells.size(); i++){
			
			cells.get(i).render(g, xoffs, yoffs, zoom);
			
		}
		
	}
	
	public PlayerCell intersects(CircularObject obj){
		
		for(int i = 0; i < cells.size(); i++){
			
			if(cells.get(i).intersects(obj)){
				
				return cells.get(i);
				
			}
			
		}
		
		return null;
		
	}
	
	public void setMousePosition(int x, int y, int xoffs, int yoffs){
		
		mousePos = new Vector(x - xoffs, y - yoffs);
		
	}
	
	public void split(){
		
		if(cells.size() < MAX_CELLS){
			
			ArrayList<PlayerCell> newCells = new ArrayList<PlayerCell>();
			
			for(int i = 0; i < cells.size(); i++){
				
				PlayerCell current = cells.get(i);
				
				Vector dir = mousePos.sub(current.getPosition()).normalized();
				
				if(game.getGameObjectManager() instanceof ServerGameObjectManager){
					
					PlayerCell cell1 = new PlayerCell(this, current.getPosition(), new Vector(0, 0), color, name, cells.get(i).getMass() / 2);
					PlayerCell cell2 = new PlayerCell(this, current.getPosition().add(dir), dir.mult(100), color, name, cells.get(i).getMass() / 2);
					
					cell1.setId(ID.newId((ServerGameObjectManager)game.getGameObjectManager(), cell1));
					cell2.setId(ID.newId((ServerGameObjectManager)game.getGameObjectManager(), cell2));
					
					newCells.add(cell1);
					newCells.add(cell2);
					
				}
				
			}
			
			GameObjectManager manager = game.getGameObjectManager();
			
			if(manager instanceof ServerGameObjectManager){
				
				for(int i = 0; i < cells.size(); i++){
					
					((ServerGameObjectManager)manager).removeGameObject(cells.get(i));
					
				}
				
			}
			
			cells = newCells;
			
		}
		
	}
	
	public void launchFood(){
		
		for(int i = 0; i < cells.size(); i++){
			
			PlayerCell current = cells.get(i);
			
			if(current.getMass() > Food.MASS){
				
				Vector cellPos = current.getPosition();
				
				Vector between = mousePos.sub(cellPos);
				
				Vector dir = between.normalized();
				
				Vector spawnLocation = cellPos.add(dir.mult(current.getRadius() + 50.0f));
				
				if(game.getGameObjectManager() instanceof ServerGameObjectManager){
					
					Food food = new Food(spawnLocation, dir.mult(Food.LAUNCH_SPEED), color);
					
					food.setId(ID.newId((ServerGameObjectManager)game.getGameObjectManager(), food));
					
					game.getGameObjectManager().addGameObject(food);
					
				}
				
				current.setMass(current.getMass() - Food.MASS);
				
			}
			
		}
		
	}
	
	public void launchVirusBomb(){
		
		PlayerCell largest = cells.get(0);
		
		for(int i = 0; i < cells.size(); i++){
			
			PlayerCell current = cells.get(i);
			
			if(current.getMass() > largest.getMass()){
				
				largest = current;
				
			}
			
		}
		
		if(largest.getMass() <= VirusBomb.MASS){
			
			return;
			
		}
		
		Vector cellPos = largest.getPosition();
		
		Vector between = mousePos.sub(cellPos);
		
		Vector dir = between.normalized();
		
		Vector spawnLocation = cellPos.add(dir.mult(largest.getRadius() + 50.0f));
		
		if(game.getGameObjectManager() instanceof ServerGameObjectManager){
			
			VirusBomb bomb = new VirusBomb(spawnLocation, dir.mult(VirusBomb.SHOOTING_POWER), 5000);
			
			bomb.setId(ID.newId((ServerGameObjectManager)game.getGameObjectManager(), bomb));
			
			game.getGameObjectManager().addGameObject(bomb);
			
		}
		
		largest.setMass(largest.getMass() - VirusBomb.MASS);
		
	}
	
	public void removeCell(PlayerCell cell){
		
		cells.remove(cell);
		
		GameObjectManager manager = game.getGameObjectManager();
		
		if(manager instanceof ServerGameObjectManager){
			
			ServerGameObjectManager theManager = (ServerGameObjectManager)manager;
			
			theManager.removeGameObject(cell);
			
		}
		
	}
	
	public void splitCell(PlayerCell cell, int amount){
		
		int splitAmount = amount;
		
		if(cells.contains(cell)){
			
			if(cells.size() - 1 + splitAmount > MAX_CELLS){
				
				splitAmount = MAX_CELLS - (cells.size() - 1);
				
			}
			
			if(splitAmount > 0){
				
				float splitMass = cell.getMass() / splitAmount;
				
				cells.remove(cell);
				
				GameObjectManager manager = game.getGameObjectManager();
				
				if(manager instanceof ServerGameObjectManager){
					
					ServerGameObjectManager theManager = (ServerGameObjectManager)manager;
					
					theManager.removeGameObject(cell);
					
				}
				
				float angleInterval = 360.0f / splitAmount;
				
				for(int i = 0; i < splitAmount; i++){
					
					float xvelocity = (float)Math.sin(Math.toRadians(i * angleInterval));
					float yvelocity = (float)Math.cos(Math.toRadians(i * angleInterval));
					
					if(game.getGameObjectManager() instanceof ServerGameObjectManager){
						
						PlayerCell playerCell = new PlayerCell(this, cell.getPosition(), new Vector(xvelocity, yvelocity).mult(20), color, name, splitMass);
						
						playerCell.setId(ID.newId((ServerGameObjectManager)game.getGameObjectManager(), playerCell));
						
						cells.add(playerCell);
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public PlayerCell[] intersects(Player player){
		
		for(int i = 0; i < player.cells.size(); i++){
			
			PlayerCell intersected = null;
			
			if((intersected = intersects(player.cells.get(i))) != null){
				
				return new PlayerCell[]{ intersected, player.cells.get(i) };
				
			}
			
		}
		
		return null;
		
	}
	
	public void addCellChangeListener(CellChangeListener listener){
		
		this.cellChangeListener = listener;
		
	}
	
	public ArrayList<PlayerCell> getCells(){
		
		return cells;
		
	}
	
}
