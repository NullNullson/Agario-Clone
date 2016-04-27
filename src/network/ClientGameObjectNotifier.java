package network;

import java.awt.Color;
import java.util.ArrayList;

import agario.Dot;
import agario.Food;
import agario.MassiveGameObject;
import agario.PlayerCell;
import agario.Virus;
import agario.VirusBomb;
import physics.GameObject;
import physics.Vector;

/**
 * Runs on the server side and translates gameobject events into a form that can be written to a stream.
 * @author Troy
 *
 */
public class ClientGameObjectNotifier {
	
	private ArrayList<String> queue = new ArrayList<String>();
	
	private Server server;
	
	public ClientGameObjectNotifier(Server server){
		
		this.server = server;
		
	}
	
	public ClientGameObjectNotifier(){
		
		
		
	}
	
	public void setServer(Server server){
		
		this.server = server;
		
	}
	
	public void tick(){
		
		for(int i = 0; i < queue.size(); i++){
			
			server.sendInfo(queue.get(i));
			
		}
		
		queue.clear();
		
	}
	
	public void updatePosition(GameObject obj){
		
		String translated = null;
		
		Vector pos = obj.getPosition();
		
		translated = "m," + obj.getId() + ","
				+ pos.x + "," + pos.y + "\n";
		
		if(translated != null){
			
			queue.add(translated);
			
		}
		
	}
	
	public void updateMass(MassiveGameObject obj){
		
		String translated = null;
		
		float mass = obj.getMass();
		
		translated = "ma," + obj.getId() + "," + mass + "\n";
		
		if(translated != null){
			
			queue.add(translated);
			
		}
		
	}
	
	public void removeGameObject(GameObject obj){
		
		String translated = "r," + obj.getId() + "\n";
		
		queue.add(translated);
		
	}
	
	public void newGameObject(GameObject obj){
		
		String translated = null;
		
		if(obj instanceof PlayerCell){
			
			PlayerCell pc = (PlayerCell)obj;
			
			Vector pos = pc.getPosition();
			
			Color color = pc.getColor();
			
			translated = "c,pc," + obj.getId() + "," + pos.x
					+ "," + pos.y + "," + pc.getMass()
					+ "," + color.getRed() + "," + color.getGreen()
					+ "," + color.getBlue() + "," + pc.getName()
					+ "\n";
			
		}
		else if(obj instanceof Dot){
			
			Dot dot = (Dot)obj;
			
			Vector pos = dot.getPosition();
			
			Color color = dot.getColor();
			
			translated = "c,d," + obj.getId() + "," + pos.x
					+ "," + pos.y + "," + dot.getMass()
					+ "," + color.getRed() + "," + color.getGreen()
					+ "," + color.getBlue() + "\n";
			
		}
		else if(obj instanceof Virus){
			
			Virus virus = (Virus)obj;
			
			Vector pos = virus.getPosition();
			
			translated = "c,v," + obj.getId() + "," + pos.x
					+ "," + pos.y + "," + virus.getMass() + "\n";
			
		}
		else if(obj instanceof Food){
			
			Food food = (Food)obj;
			
			Vector pos = food.getPosition();
			
			Color color = food.getColor();
			
			translated = "c,f," + obj.getId() + "," + pos.x
					+ "," + pos.y + "," + food.getMass()
					+ "," + color.getRed() + "," + color.getGreen()
					+ "," + color.getBlue() + "\n";
			
		}
		else if(obj instanceof VirusBomb){
			
			VirusBomb bomb = (VirusBomb)obj;
			
			Vector pos = bomb.getPosition();
			
			translated = "c,vb," + obj.getId() + "," + pos.x
					+ "," + pos.y + "," + bomb.getDetonationTime()
					+ "\n";
			
		}
		
		if(translated != null){
			
			queue.add(translated);
			
		}
		
	}
	
	public void playerDied(){
		
		server.sendInfo("d\n");
		
	}
	
}
