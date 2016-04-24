package agario;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import physics.GameObject;
import physics.Vector;

public class PlayerCell extends CircularObject {
	
	private Color color;
	
	private String name;
	
	public static final Font nameFont = new Font("Consolas", Font.BOLD, 36);
	
	public static final float startingSpeed = 100.0f;
	
	public static final float scale = 30.0f;
	
	public static final float maxSpeed = 20.0f;
	
	public static final int combineSpeed = 160;
	
	private Vector mousePos;
	
	private float speed;
	
	private Vector velocity;
	
	private long timeWhenReadyToCombine;
	
	private boolean readyToCombine = false;
	
	public PlayerCell(int id, Vector position, Vector velocity, Color color, String name, float mass){
		
		super(position, id, mass);
		
		timeWhenReadyToCombine = System.currentTimeMillis() + (int)mass * combineSpeed;
		
		this.velocity = velocity;
		
		mousePos = new Vector(0, 0);
		
		speed = 5;
		
		this.color = color;
		
		this.name = name;
		
	}
	
	public PlayerCell(Vector position, Vector velocity, Color color, String name, float mass){
		
		this(0, position, velocity, color, name, mass);
		
	}
	
	public void tick(){
		
		if(timeWhenReadyToCombine <= System.currentTimeMillis()){
			
			readyToCombine = true;
			
		}
		
		Vector playerPos = getPosition();
		
		if(mousePos == null){
			
			mousePos = new Vector(500, 500);
			
		}
		
		Vector dist = mousePos.sub(playerPos);
		
		Vector dir = dist.normalized();
		
		setPosition(getPosition().add(dir.mult(speed)).add(velocity));
		
		velocity = velocity.div(1.1f);
		
	}
	
	public void render(Graphics g, int xoffs, int yoffs, float zoom){
		
		Vector pos = getPosition();
		
		g.setColor(color);
		
		double radius = Math.sqrt(mass / Math.PI) * scale;
		
		setRadius((float)radius);
		
		double diameter = 2 * radius;
		
		g.fillOval((int)((pos.x + xoffs - (int)radius) * zoom), (int)((pos.y + yoffs - (int)radius) * zoom), (int)(diameter * zoom), (int)(diameter * zoom));
		
		g.setColor(Color.black);
		
		g.setFont(nameFont);
		
		int nameWidth = g.getFontMetrics().stringWidth(name);
		
		g.drawString(name, (int)((pos.x + xoffs - nameWidth / 2) * zoom), (int)((pos.y + yoffs) * zoom));
		
	}
	
	public void setMousePosition(Vector position){
		
		this.mousePos = position;
		
	}
	
	public boolean isReadyToCombine(){
		
		return readyToCombine;
		
	}
	
	public void setReadyToCombine(boolean readyToCombine){
		
		this.readyToCombine = readyToCombine;
		
	}
	
	public Color getColor(){
		
		return color;
		
	}
	
	public String getName(){
		
		return name;
		
	}
	
}
