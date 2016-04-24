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
	
	public Server(AgarioParser parser){
		
		this.parser = parser;
		
		try{
			
			serverSocket = new ServerSocket(PORT);
			
			client = new ClientReference(serverSocket.accept());
			
			out = new DataOutputStream(client.getSocket().getOutputStream());
			
			out.flush();
			
			in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
		new Thread(new Runnable(){
			
			public void run(){
				
				while(true){
					
					readInfo();
					
				}
				
			}
			
		}).start();
		
	}
	
	private void readInfo(){
		
		try{
			
			String info = in.readLine();
			
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
	
}
