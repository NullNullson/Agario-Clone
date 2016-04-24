package agario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import main.Main;
import managers.ClientGameObjectManager;
import managers.ServerGameObjectManager;
import network.AgarioParser;
import network.ClientGameObjectNotifier;
import network.Server;
import physics.Vector;

public class ServerGame extends Game implements MouseWheelListener, MouseMotionListener, KeyListener{
	
	private Spawner spawner;
	
	private Player player;
	
	private final int gridInterval = 100;
	
	private float zoom = 1;
	
	public static final int WORLD_WIDTH = 1000;
	
	public static final int WORLD_HEIGHT = 1000;
	
	private Server server;
	
	private AgarioParser parser;
	
	private ClientGameObjectNotifier notifier;
	
	public ServerGame(Main main){
		
		super(main);
		
		main.addMouseWheelListener(this);
		
		main.addMouseMotionListener(this);
		
		main.addKeyListener(this);
		
		notifier = new ClientGameObjectNotifier();
		
		manager = new ServerGameObjectManager(notifier);
		
		player = new Player(this, new Vector(0, 0), Color.red, "Player");
		
		player.setId(ID.newId((ServerGameObjectManager)manager, player));
		
		parser = new AgarioParser(manager, this, GameMode.SERVER);
		
		server = new Server(parser);
		
		notifier.setServer(server);
		
		spawner = new Spawner(manager, 300, 1000);
		
		manager.addGameObject(player);
		
	}
	
	public void tick(){
		
		parser.tick();
		
		Vector playerPos = player.getPosition();
		
		xoffs = -(int)playerPos.x + Main.WIDTH / 2;
		yoffs = -(int)playerPos.y + Main.HEIGHT / 2;
		
		manager.tick();
		
		notifier.tick();
		
		spawner.tick();
		
	}
	
	public void render(Graphics g){
		
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
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			
			player.split();
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_W){
			
			player.launchFood();
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		player.setMousePosition(e.getX(), e.getY(), xoffs, yoffs);
		
	}
	
	public Server getServer(){
		
		return server;
		
	}
	
}
