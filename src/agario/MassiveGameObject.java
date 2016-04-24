package agario;

import physics.GameObject;
import physics.Vector;

public abstract class MassiveGameObject extends GameObject{
	
	protected float mass;
	
	private float prevMass;
	
	private MassChangeListener listener;
	
	public MassiveGameObject(Vector position, int id, float mass){
		
		super(position, id);
		
		this.mass = mass;
		
		prevMass = mass;
		
	}
	
	public float getMass(){
		
		return mass;
		
	}
	
	public void setMass(float mass){
		
		prevMass = this.mass;
		
		this.mass = mass;
		
		if(prevMass != mass && listener != null){
			
			listener.onMassChange(this);
			
		}
		
	}
	
	public void addMassChangeListener(MassChangeListener listener){
		
		this.listener = listener;
		
	}
	
}
