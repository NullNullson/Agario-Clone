package gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import main.Main;

public abstract class RectangularGuiComponent extends GuiComponent{
	
	private boolean hoveredOver = false;
	
	protected Rectangle rectangle;
	
	private ClickListener clickListener;
	
	private EnterListener enterListener;
	
	private ExitListener exitListener;
	
	public RectangularGuiComponent(Main main, String name, Rectangle rectangle){
		
		super(main, name);
		
		this.rectangle = rectangle;
		
	}
	
	public void addClickListener(ClickListener listener){
		
		this.clickListener = listener;
		
	}
	
	public void removeClickListener(){
		
		clickListener = null;
		
	}
	
	public void addEnterListener(EnterListener listener){
		
		this.enterListener = listener;
		
	}
	
	public void removeEnterListener(){
		
		enterListener = null;
		
	}
	
	public void addExitListener(ExitListener listener){
		
		this.exitListener = listener;
		
	}
	
	public void removeExitListener(){
		
		exitListener = null;
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if(rectangle.contains(e.getPoint())){
			
			if(clickListener != null){
				
				clickListener.onClick(name);
				
			}
			
		}
		
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
		
		if(rectangle.contains(e.getPoint()) && !hoveredOver){
			
			hoveredOver = true;
			
			if(enterListener != null){
				
				enterListener.onMouseEnter(name);
				
			}
			
		}
		else if(!rectangle.contains(e.getPoint()) && hoveredOver){
			
			hoveredOver = false;
			
			if(exitListener != null){
				
				exitListener.onMouseExit(name);
				
			}
			
		}
		
	}
	
	public boolean isHoveredOver(){
		
		return hoveredOver;
		
	}
	
}
