package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.Socket;

import javax.swing.JOptionPane;

import gamestate.MenuState;
import main.Main;

public class Client implements UncaughtExceptionHandler{
	
	private Socket server;
	
	private BufferedReader in;
	
	private DataOutputStream out;
	
	private AgarioParser parser;
	
	private DisconnectListener listener;
	
	private boolean connected = false;
	
	public Client(Main main, AgarioParser parser, String ip) throws InvalidIPException{
		
		this.parser = parser;
		
		new Thread(new Runnable(){
			
			public void run(){
				
				try{
					
					server = new Socket(ip, Server.PORT);
					
					in = new BufferedReader(new InputStreamReader(server.getInputStream()));
					
					out = new DataOutputStream(server.getOutputStream());
					
					out.flush();
					
					connected = true;
					
				}
				catch(IOException e){
					
					main.setState(new MenuState(main));
					
					JOptionPane.showMessageDialog(main, "Error: could not connect to server");
					
					return;
					
				}
				
				while(connected){
					
					readInfo();
					
				}
				
			}
			
		}).start();
		
	}
	
	private void readInfo(){
		
		try{
			
			String info = in.readLine();
			
			if(info == null){
				
				// Server disconnected, disconnect client as well
				disconnect();
				
				if(listener != null){
					
					listener.onDisconnect();
					
				}
				
			}
				
			parser.addToQueue(info);
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
	}
	
	public void writeInfo(String info){
		
		try{
			
			out.write(info.getBytes());
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
	}
	
	public void addDisconnectListener(DisconnectListener listener){
		
		this.listener = listener;
		
	}
	
	public void disconnect(){
		
		connected = false;
		
		try{
			
			in.close();
			out.close();
			server.close();
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
	}
	
	public boolean isConnected(){
		
		return connected;
		
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		
		try {
			
			throw e;
			
		} catch (Throwable e1) {
			
			e1.printStackTrace();
			
		}
		
	}
	
}
