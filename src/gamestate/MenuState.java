package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

import gui.Button;
import gui.ClickListener;
import gui.FlowLayout;
import gui.GuiManager;
import main.Main;
import network.InvalidIPException;

public class MenuState extends GameState implements ClickListener{
	
	private GuiManager manager;
	
	private Button hostServer;
	
	private Button joinServer;
	
	private Main main;
	
	public MenuState(Main main){
		
		this.main = main;
		
		manager = new GuiManager();
		
		hostServer = new Button(main, "host", "Host Server", 100, 100);
		
		joinServer = new Button(main, "join", "Join Server", 600, 100);
		
		hostServer.addClickListener(this);
		
		joinServer.addClickListener(this);
		
		FlowLayout layout = new FlowLayout(main, "layout", 0, 300);
		
		layout.addComponent(hostServer);
		
		layout.addComponent(joinServer);
		
		manager.addGuiComponent(layout);
		
	}
	
	public void destroy(){
		
		hostServer.removeClickListener();
		
		joinServer.removeClickListener();
		
	}
	
	public void tick(){
		
		manager.tick();
		
	}
	
	public void render(Graphics g){
		
		g.setColor(Color.black);
		
		manager.render(g);
		
	}
	
	@Override
	public void onClick(String name) {
		
		if(name.equals("host")){
			
			main.setState(new ServerGame(main));
			
		}
		else if(name.equals("join")){
			
			String ip = JOptionPane.showInputDialog(main, "Enter server IP:");
			
			ClientGame game = null;
			
			try{
				
				game = new ClientGame(main, ip);
				
				main.setState(game);
				
			}
			catch(InvalidIPException e){
				
				JOptionPane.showMessageDialog(main, "Error: could not connect to IP address");
				
			}
			
		}
		
	}
	
}
