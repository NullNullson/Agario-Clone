package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import agario.GameMode;
import main.Main;
import managers.ClientGameObjectManager;
import network.AgarioParser;
import network.Client;
import network.InvalidIPException;
import physics.Vector;

public class ClientGame extends MainGameState implements MouseWheelListener, MouseMotionListener, KeyListener{
	
	private final int gridInterval = 100;
	
	private float zoom = 1;
	
	private int xoffs;
	
	private int yoffs;
	
	private Client client;
	
	private AgarioParser parser;
	
	private int mouseX = 10;
	
	private int mouseY = 10;
	
	private long startTime = System.currentTimeMillis();
	
	private boolean initialized = false;
	
	public ClientGame(Main main, String ip) throws InvalidIPException{
		
		super(main);
		
		main.addMouseWheelListener(this);
		
		main.addMouseMotionListener(this);
		
		main.addKeyListener(this);
		
		manager = new ClientGameObjectManager();
		
		parser = new AgarioParser(main, manager, this, GameMode.CLIENT);
		
		client = new Client(main, parser, ip);
		
	}
	
	private void init(){
		
		initialized = true;
		
		client.writeInfo("handshake\n");
		
	}
	
	public void tick(){
		
		if(client.isConnected()){
			
			if(!initialized){
				
				init();
				
			}
			
			client.writeInfo(mouseX + "," + mouseY + "\n");
			
			parser.tick();
			
			manager.tick();
			
		}
		
	}
	
	public void render(Graphics g){
		
		if(client.isConnected()){
			
			g.setColor(Color.gray);
			
			// Draw grid background
			for(int x = 0; x < main.getWidth() / gridInterval + 5; x++){
				
				g.drawLine((int)((x * gridInterval + xoffs % gridInterval) * zoom), 0, (int)((x * gridInterval + xoffs % gridInterval) * zoom), main.getHeight());
				
			}
			
			for(int y = 0; y < main.getHeight() / gridInterval + 5; y++){
				
				g.drawLine(0, (int)((y * gridInterval + yoffs % gridInterval) * zoom), main.getWidth(), (int)((y * gridInterval + yoffs % gridInterval) * zoom));
				
			}
			
			manager.render(g, xoffs, yoffs, zoom);
			
		}
		else{
			
			g.setColor(Color.black);
			
			g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			g.setColor(Color.white);
			
			String connecting = "Connecting to server...";
			
			int stringWidth = g.getFontMetrics().stringWidth(connecting);
			
			int stringHeight = 20;
			
			g.drawString(connecting, Main.WIDTH / 2 - stringWidth / 2, Main.HEIGHT / 2 - stringHeight / 2);
			
			int xpos = (int)((System.currentTimeMillis() - startTime) / 10.0) % Main.WIDTH;
			
			for(int i = 0; i < 3; i++){
				
				g.fillOval(xpos + i * 50, 800, 10, 10);
				
			}
			
		}
		
	}
	
	public float getZoom(){
		
		return zoom;
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		/*int wheelRotation = e.getWheelRotation();
		
		if(wheelRotation > 0){
			
			zoom *= 2;
			
		}
		else{
			
			zoom /= 2;
			
		}*/
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		mouseX = e.getX();
		
		mouseY = e.getY();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_W){
			
			client.writeInfo("w\n");
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE){
			
			client.writeInfo("space\n");
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void setOffset(int xoffs, int yoffs){
		
		this.xoffs = xoffs;
		
		this.yoffs = yoffs;
		
	}
	
	@Override
	public void destroy() {
		
		if(client.isConnected()){
			
			client.disconnect();
			
		}
		
	}
	
}
