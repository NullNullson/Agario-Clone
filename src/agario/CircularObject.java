package agario;

import java.awt.Graphics;

import physics.GameObject;
import physics.Vector;

public abstract class CircularObject extends MassiveGameObject{
	
	private float radius;
	
	public CircularObject(Vector position, int id, float mass){
		
		super(position, id, mass);
		
	}
	
	public boolean intersects(CircularObject obj){
		
		Vector thisPosition = getPosition();
		
		Vector otherPosition = obj.getPosition();
		
		Vector between = thisPosition.sub(otherPosition);
		
		return between.magnitude() <= radius + obj.getRadius();
		
	}
	
	public boolean withinEatingProximity(CircularObject obj){
		
		Vector thisPosition = getPosition();
		
		Vector otherPosition = obj.getPosition();
		
		Vector between = thisPosition.sub(otherPosition);
		
		if(obj.getRadius() < radius){
			
			return between.magnitude() <= radius + obj.getRadius() * 0.5;
			
		}
		else{
			
			return between.magnitude() <= radius * 0.5 + obj.getRadius();
			
		}
		
	}
	
	public float getRadius(){
		return radius;
	}
	
	public void setRadius(float radius){
		this.radius = radius;
	}
	
}
