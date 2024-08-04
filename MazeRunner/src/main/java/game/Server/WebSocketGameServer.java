package game.Server;

import static game.UtilityAndConstant.ConstantAndDefine.HOST_NAME;
import static game.UtilityAndConstant.ConstantAndDefine.ROOT_URL;
import static game.UtilityAndConstant.ConstantAndDefine.SERVER_PORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;

import jakarta.websocket.DeploymentException;

public class WebSocketGameServer {
	
	public static void main(String[] args)
	{
		System.out.println("Please enter the server IP Address :");
		String serverAddress = HOST_NAME;
		try {
			serverAddress = new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			if (serverAddress.isEmpty())
			{
				serverAddress = HOST_NAME;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Server server = new Server(serverAddress, SERVER_PORT, ROOT_URL, null, GameServer.class);
	
	    try {
	    	server.start();
	    	System.out.println("Press enter to stop the server...");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
	    }
	    catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        server.stop();
	    }
	}
}

