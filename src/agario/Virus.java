package agario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import managers.ClientGameObjectManager;
import managers.GameObjectManager;
import managers.ServerGameObjectManager;
import physics.Vector;

public class Virus extends CircularObject{
	
	public static final float STARTING_MASS = 10.0f;
	
	public static final float scale = PlayerCell.scale;
	
	public static final float SHOOTING_THRESHOLD = 20;	
	
	public static final float SHOOTING_POWER = 30.0f;
	
	public static Image IMAGE;
	
	static{
		
		try {
			
			IMAGE = ImageIO.read(new File("res/virus.png"));
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private Vector velocity;
	
	public Virus(int id, Vector position){
		
		super(position, id, Virus.STARTING_MASS);
		
		velocity = new Vector(0, 0);
		
	}
	
	public Virus(int id, Vector position, Vector velocity){
		
		super(position, id, Virus.STARTING_MASS);
		
		this.velocity = velocity;
		
	}
	
	public Virus(Vector position){
		
		this(0, position);
		
	}
	
	public Virus(Vector position, Vector velocity){
		
		this(0, position, velocity);
		
	}
	
	public void tick(){
		
		setPosition(getPosition().add(velocity));
		
		velocity = velocity.div(1.1f);
		
	}
	
	public void render(Graphics g, int xoffs, int yoffs, float zoom){
		
		Vector pos = getPosition();
		
		double radius = Math.sqrt(mass / Math.PI) * scale;
		
		setRadius((float)radius);
		
		double diameter = 2 * radius;
		
		g.drawImage(IMAGE, (int)((pos.x + xoffs - (int)radius) * zoom), (int)((pos.y + yoffs - (int)radius) * zoom), (int)(diameter * zoom), (int)(diameter * zoom), null);
		
	}
	
	public void eatFood(Food food, GameObjectManager manager){
		
		mass += food.getMass();
		
		if(mass >= SHOOTING_THRESHOLD){
			
			Vector foodPos = food.getPosition();
			
			Vector between = getPosition().sub(foodPos);
			
			Vector dir = between.normalized();
			
			if(manager instanceof ServerGameObjectManager){
				
				Virus newVirus = new Virus(getPosition(), dir.mult(SHOOTING_POWER));
				
				newVirus.setId(ID.newId((ServerGameObjectManager)manager, newVirus));
				
				newVirus.setMass(mass - 10);
				
				setMass(10);
				
				manager.addGameObject(newVirus);
				
			}
			
		}
		
	}
	
}
