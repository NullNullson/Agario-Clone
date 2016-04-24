package gui;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.Main;

public abstract class GuiComponent implements MouseListener, MouseMotionListener{
	
	protected String name;
	
	protected Main main;
	
	public GuiComponent(Main main, String name){
		
		this.main = main;
		
		main.addMouseListener(this);
		
		main.addMouseMotionListener(this);
		
		this.name = name;
		
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public String getName(){
		
		return name;
		
	}
	
}
