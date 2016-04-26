package physics;

import java.awt.Point;
import java.io.Serializable;

public class Vector implements Serializable{
	
	public float x, y;
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector(Point p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public Vector add(Vector vec){
		return new Vector(this.x + vec.x, this.y + vec.y);
	}
	
	public Vector sub(Vector vec){
		return new Vector(this.x - vec.x, this.y - vec.y);
	}
	
	public void clear(){
		this.x = this.y = 0;
	}
	
	public Vector normalized(){
		float mag = magnitude();
		return new Vector(this.x / mag, this.y / mag);
	}
	
	public Vector mult(Vector vec){
		return new Vector(this.x * vec.x, this.y * vec.y);
	}
	
	public Vector mult(float scalar){
		return new Vector(this.x * scalar, this.y * scalar);
	}
	
	public Vector div(float scalar){
		return new Vector(this.x / scalar, this.y / scalar);
	}
	
	public float magnitude(){
		return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public String toString(){
		return this.x + ", " + this.y;
	}
	
	public boolean equals(Vector vec){
		return this.x == vec.x && this.y == vec.y;
	}
	
	public static Vector random(){
		
		return new Vector((float)(Math.random() * 100 - 50), (float)(Math.random() * 100 - 50));
		
	}
	
}
