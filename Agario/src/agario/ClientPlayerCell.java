package agario;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import physics.GameObject;
import physics.Vector;

public class ClientPlayerCell extends CircularObject {
	
	private Color color;
	
	private String name;
	
	public static final float scale = 30.0f;
	
	public static final Font nameFont = new Font("Consolas", Font.BOLD, 36);
	
	public ClientPlayerCell(int id, Vector position, Vector velocity, Color color, String name, float mass){
		
		super(position, id, mass);
		
		this.color = color;
		
		this.name = name;
		
	}
	
	public ClientPlayerCell(Vector position, Vector velocity, Color color, String name, float mass){
		
		this(0, position, velocity, color, name, mass);
		
	}
	
	public void tick(){
		
		
		
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
	
	public Color getColor(){
		
		return color;
		
	}
	
	public String getName(){
		
		return name;
		
	}
	
}
