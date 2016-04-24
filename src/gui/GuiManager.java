package gui;

import java.awt.Graphics;
import java.util.ArrayList;

public class GuiManager {
	
	private ArrayList<GuiComponent> components = new ArrayList<GuiComponent>();
	
	public GuiManager(){
		
	}
	
	public void tick(){
		
		for(int i = 0; i < components.size(); i++){
			
			components.get(i).tick();
			
		}
		
	}
	
	public void render(Graphics g){
		
		for(int i = 0; i < components.size(); i++){
			
			components.get(i).render(g);
			
		}
		
	}
	
	public void addGuiComponent(GuiComponent component){
		
		components.add(component);
		
	}
	
}
