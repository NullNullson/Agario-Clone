package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	
	private Socket server;
	
	private BufferedReader in;
	
	private DataOutputStream out;
	
	private AgarioParser parser;
	
	public Client(AgarioParser parser, String ip) throws InvalidIPException{
		
		this.parser = parser;
		
		try{
			
			server = new Socket("localhost", Server.PORT);
			
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			
			out = new DataOutputStream(server.getOutputStream());
			
			out.flush();
			
		}
		catch(IOException e){
			
			throw new InvalidIPException();
			
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
			
			if(info != null){
				
				parser.addToQueue(info);
				
			}
			
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
	
}
