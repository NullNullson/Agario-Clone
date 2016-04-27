package agario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import physics.Vector;

public class VirusBomb extends CircularObject{
	
	public static final float scale = (float)(PlayerCell.scale * 0.5);
	
	public static final float SHOOTING_POWER = 30.0f;
	
	public static final int NUM_VIRUSES = 10;
	
	public static final float MAX_LAUNCH_SPEED = 1.0f;
	
	public static final float MIN_LAUNCH_SPEED = 0.8f;
	
	public static final float MASS = Virus.STARTING_MASS * NUM_VIRUSES;
	
	public static Image IMAGE;
	
	static{
		
		try {
			
			IMAGE = ImageIO.read(new File("res/bomb.png"));
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private Vector velocity;
	
	private final int originalDetonationTime;
	
	private long detonationTime;
	
	private boolean detonated = false;
	
	private DecimalFormat detonationTextFormat;
	
	public VirusBomb(int id, Vector position, int detonationTime){
		
		super(position, id, VirusBomb.MASS);
		
		originalDetonationTime = detonationTime;
		
		detonationTextFormat = new DecimalFormat("0.0");
		
		this.detonationTime = System.currentTimeMillis() + detonationTime;
		
		velocity = new Vector(0, 0);
		
	}
	
	public VirusBomb(int id, Vector position, Vector velocity, int detonationTime){
		
		super(position, id, VirusBomb.MASS);
		
		originalDetonationTime = detonationTime;
		
		detonationTextFormat = new DecimalFormat("0.0");
		
		this.detonationTime = System.currentTimeMillis() + detonationTime;
		
		this.velocity = velocity;
		
	}
	
	public VirusBomb(Vector position, int detonationTime){
		
		this(0, position, detonationTime);
		
	}
	
	public VirusBomb(Vector position, Vector velocity, int detonationTime){
		
		this(0, position, velocity, detonationTime);
		
	}
	
	public void tick(){
		
		if(System.currentTimeMillis() >= detonationTime){
			
			detonated = true;
			
		}
		
		setPosition(getPosition().add(velocity));
		
		velocity = velocity.div(1.1f);
		
	}
	
	public void render(Graphics g, int xoffs, int yoffs, float zoom){

		Vector pos = getPosition();
		
		double radius = Math.sqrt(mass / Math.PI) * scale;
		
		setRadius((float)radius);
		
		double diameter = 2 * radius;
		
		int x = (int)((pos.x + xoffs - (int)radius) * zoom);
		
		int y = (int)((pos.y + yoffs - (int)radius) * zoom);
		
		int w = (int)(diameter * zoom);
		
		int h = (int)(diameter * zoom);
		
		g.drawImage(IMAGE, x, y, w, h, null);
		
		g.setColor(Color.red);
		
		String text = detonationTextFormat.format((detonationTime - System.currentTimeMillis()) / 1000.0);
		
		g.drawString(text, x + w / 2 - 20, y + h / 2 + 10);
		
	}
	
	public int getDetonationTime(){
		
		return originalDetonationTime;
		
	}
	
	public boolean readyToDetonate(){
		
		return detonated;
		
	}
	
}
