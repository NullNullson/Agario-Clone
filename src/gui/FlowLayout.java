package gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import main.Main;

public class FlowLayout extends GuiComponent{
	
	private ArrayList<RectangularGuiComponent> components;
	
	private int y;
	
	private int height;
	
	public FlowLayout(Main main, String name, int y, int height, ArrayList<RectangularGuiComponent> components){
		
		super(main, name);
		
		this.y = y;
		
		this.height = height;
		
		this.components = components;
		
		updateComponentPositions();
		
	}
	
	public FlowLayout(Main main, String name, int y, int height){
		
		this(main, name, y, height, new ArrayList<RectangularGuiComponent>());
		
	}
	
	public void addComponent(RectangularGuiComponent component){
		
		components.add(component);
		
		updateComponentPositions();
		
	}
	
	private void updateComponentPositions(){
		
		int numComponents = components.size();
		
		if(numComponents > 0){
			
			int interval = Main.WIDTH / numComponents;
			
			for(int i = 0; i < numComponents; i++){
				
				RectangularGuiComponent current = components.get(i);
				
				current.rectangle.y = y;
				current.rectangle.height = height;
				current.rectangle.x = interval * i;
				current.rectangle.width = interval;
				
			}
			
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void tick() {
		
		for(int i = 0; i < components.size(); i++){
			
			components.get(i).tick();
			
		}
		
	}
	
	@Override
	public void render(Graphics g) {
		
		for(int i = 0; i < components.size(); i++){
			
			components.get(i).render(g);
			
		}
		
	}
	
}
