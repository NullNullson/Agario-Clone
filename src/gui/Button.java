package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.Main;

public class Button extends RectangularGuiComponent{
	
	private String text;
	
	private int alpha = 0;
	
	public static final int ARC_SIZE = 20;
	
	public static final int MAX_FADE_ALPHA = 127;
	
	public static final int FADE_SPEED = 30;
	
	public static final Font textFont = new Font("Consolas", Font.BOLD, 36);
	
	/**
	 * Make a new button where rect is the bounding rectangle
	 * and name is the name of the button.
	 * @param rect
	 * @param name
	 * @param main
	 */
	public Button(Main main, String name, Rectangle rect){
		
		super(main, name, rect);
		
	}
	
	/**
	 * Make a new button with the width and height already predefined.
	 * @param main
	 * @param name
	 * @param x
	 * @param y
	 */
	public Button(Main main, String name, String text, int x, int y){
		
		this(main, name, new Rectangle(x, y, 200, 50));
		
		this.text = text;
		
	}
	
	public void tick(){
		
		boolean hovered = isHoveredOver();
		
		if(hovered && alpha < MAX_FADE_ALPHA){
			
			alpha += FADE_SPEED;
			
		}
		
		if(!hovered && alpha > 0){
			
			alpha -= FADE_SPEED;
			
		}
		
		if(alpha < 0){
			
			alpha = 0;
			
		}
		
		if(alpha > MAX_FADE_ALPHA){
			
			alpha = MAX_FADE_ALPHA;
			
		}
		
	}
	
	public void render(Graphics g){
		
		g.setColor(Color.black);
		
		g.drawRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, ARC_SIZE, ARC_SIZE);
		
		int stringWidth = g.getFontMetrics().stringWidth(text);
		
		int stringHeight = 10;
		
		g.setFont(textFont);
		
		g.drawString(text, rectangle.x + rectangle.width / 2 - stringWidth / 2, rectangle.y + rectangle.height / 2 - stringHeight / 2);
		
		if(alpha != 0){
			
			int borderWidth = 5;
			
			g.setColor(new Color(0, 0, 255, alpha));
			
			g.fillRoundRect(rectangle.x - borderWidth, rectangle.y - borderWidth, rectangle.width + borderWidth * 2, rectangle.height + borderWidth * 2, ARC_SIZE, ARC_SIZE);
			
		}
		
	}
	
}
