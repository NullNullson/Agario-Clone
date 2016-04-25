package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static final int PORT = 3274;
	
	private ServerSocket serverSocket;
	
	private ClientReference client;
	
	private DataOutputStream out;
	
	private BufferedReader in;
	
	private AgarioParser parser;
	
	private DisconnectListener listener;
	
	private boolean connected = false;
	
	public Server(AgarioParser parser){
		
		this.parser = parser;
		
		new Thread(new Runnable(){
			
			public void run(){
				
				try{
					
					serverSocket = new ServerSocket(PORT);
					
					client = new ClientReference(serverSocket.accept());
					
					out = new DataOutputStream(client.getSocket().getOutputStream());
					
					out.flush();
					
					in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
					
					connected = true;
					
				}
				catch(IOException e){
					
					e.printStackTrace();
					
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
				
				// Client disconnected, disconnect server as well
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
	
	public void sendInfo(String info){
		
		try{
			
			out.write(info.getBytes());
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
	}
	
	public int getClientId(){
		
		return client.getId();
		
	}
	
	public void addDisconnectListener(DisconnectListener listener){
		
		this.listener = listener;
		
	}
	
	public void disconnect(){
		
		connected = false;
		
		try{
			
			in.close();
			out.close();
			client.getSocket().close();
			serverSocket.close();
			
		}
		catch(IOException ex){
			
			ex.printStackTrace();
			
		}
		
	}
	
	public boolean isConnected(){
		
		return connected;
		
	}
	
}
