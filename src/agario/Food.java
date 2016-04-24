package agario;

import java.awt.Color;
import java.awt.Graphics;

import physics.Vector;

public class Food extends CircularObject{
	
	public static final int MASS = 3;
	
	public static final float scale = PlayerCell.scale;
	
	public static final float LAUNCH_SPEED = 20.0f;
	
	private Color color;
	
	private Vector velocity;
	
	public Food(int id, Vector position, Vector velocity, Color color){
		
		super(position, id, Food.MASS);
		
		this.velocity = velocity;
		
		this.color = color;
		
	}
	
	public Food(Vector position, Vector velocity, Color color){
		
		this(0, position, velocity, color);
		
	}
	
	public void tick(){
		
		setPosition(getPosition().add(velocity));
		
		velocity = velocity.div(1.1f);
		
	}
	
	public void render(Graphics g, int xoffs, int yoffs, float zoom){

		Vector pos = getPosition();
		
		g.setColor(color);
		
		double radius = Math.sqrt(MASS / Math.PI) * scale;
		
		setRadius((float)radius);
		
		double diameter = 2 * radius;
		
		g.fillOval((int)((pos.x + xoffs - (int)radius) * zoom), (int)((pos.y + yoffs - (int)radius) * zoom), (int)(diameter * zoom), (int)(diameter * zoom));
		
	}
	
	public Color getColor(){
		
		return color;
		
	}
	
}
