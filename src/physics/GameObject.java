package physics;

import java.awt.Graphics;

import agario.PositionChangeListener;

public abstract class GameObject {
	
	private Vector prevPosition;
	
	private Vector position;
	
	private int id;
	
	private PositionChangeListener listener;
	
	protected GameObject(Vector position, int id){
		
		this.position = position;
		
		prevPosition = position;
		
		this.id = id;
		
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g, int xoffs, int yoffs, float zoom);
	
	public void setPosition(Vector position){
		
		prevPosition = this.position;
		
		if(!prevPosition.equals(position) && listener != null){
			
			listener.onPositionChange(this);
			
		}
		
		this.position = position;
		
	}
	
	public Vector getPosition(){
		return position;
	}
	
	public int getId(){
		
		return id;
		
	}
	
	public void setId(int id){
		
		this.id = id;
		
	}
	
	public void addPositionChangeListener(PositionChangeListener listener){
		
		this.listener = listener;
		
	}
	
}
