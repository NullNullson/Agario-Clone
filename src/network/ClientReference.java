package network;

import java.net.Socket;

import agario.ID;

/**
 * A server's reference to a client, contains a socket and a
 * player gameobject id.
 * @author Troy
 *
 */
public class ClientReference {
	
	private Socket socket;
	
	private int id;
	
	public ClientReference(Socket socket){
		
		this.socket = socket;
		
	}
	
	public Socket getSocket(){
		
		return socket;
		
	}
	
	public int getId(){
		
		return id;
		
	}
	
}
