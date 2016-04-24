package agario;

import managers.ServerGameObjectManager;
import physics.GameObject;

public class ID {
	
	private static int currentId = 0;
	
	public static int newId(ServerGameObjectManager manager, GameObject obj){
		
		manager.notifyOfCreation(obj);
		
		return currentId++;
		
	}
	
	public static int newUnregisteredId(){
		
		return currentId++;
		
	}
	
}
