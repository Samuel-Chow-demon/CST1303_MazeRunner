package game.Server;

import static game.UtilityAndConstant.ConstantAndDefine.COMMAND_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_KEY_CLOSE;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_KEY_ID;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.eP1;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.eP2;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.eP3;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.eP4;

import static game.UtilityAndConstant.ConstantAndDefine.MAP_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_KEY_REQUEST;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_END_KEY;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import game.Maze.MazeData;
import game.Maze.ServerMazeManager;
import game.Player.Player;
import game.UtilityAndConstant.ConstantAndDefine.eMAP;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;
import game.UtilityAndConstant.DataFormatCenter;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/mazegame")
public class GameServer 
{    
    private static ConcurrentHashMap<String, PlayerHandler> mapPlayer;
    private static CopyOnWriteArrayList<Integer> availablePlayerList = new CopyOnWriteArrayList<>(List.of(eP1.ordinal(), eP2.ordinal(), eP3.ordinal(), eP4.ordinal()));
    
    private final Object lockPlayerConnect = new Object();
    
    private static ServerMazeManager mazeManager = new ServerMazeManager();

    @OnOpen
    public void onOpen(Session session) 
    {
    	// Prevent multiple connection reach at the same time
    	synchronized(this.lockPlayerConnect)
    	{
	    	if (mapPlayer == null)
	    	{
	    		mapPlayer = new ConcurrentHashMap<>();
	    	}
	        
	    	if (availablePlayerList.size() > 0)
	    	{
	    		// Assign Player ID
	    		Integer ePlayerID = availablePlayerList.remove(0); // pop the first one
	    		
	    		// Create PlayerHandler and store to the map with session id
	    		mapPlayer.put(session.getId(), new PlayerHandler(session, ePLAYER.fromValue(ePlayerID)));
	    		
		    	// Send the ID to the Client Player
		        Map<String, String> mapSetID = Map.of(ENTITY_KEY_ID, String.valueOf(ePlayerID));
		        try {
					session.getBasicRemote().sendBinary(DataFormatCenter.createKeyByteBufferData(COMMAND_DATA_KEY, mapSetID));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        // For later joined session (Client) need to get back all the previous client last data
		        // For rendering
		        for (PlayerHandler playerObj : mapPlayer.values())
		        {
		        	try {
		        		if (playerObj.getPlayerLastData() != null)
		        		{
		        			session.getBasicRemote().sendBinary(playerObj.getPlayerLastData());
		        		}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		        
		        System.out.println("New connection opened, Client ID : " + session.getId());
	    	}
	    	// Else if max player reach send command back to attempt client to close
	    	else
	    	{
	    		Map<String, String> mapSetClose = Map.of(ENTITY_KEY_CLOSE, "");
	    		try {
	    			session.getBasicRemote().sendBinary(DataFormatCenter.createKeyByteBufferData(COMMAND_DATA_KEY, mapSetClose));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		System.out.println("[MAX Player reached] New connection attemped, Client ID : " + session.getId());
	    	}
    	}
    }

    @OnMessage
    public void onMessage(ByteBuffer data, Session session) 
    {
		Map<String, ByteBuffer> mapDataType = DataFormatCenter.getDataStream(data);
				
		for (Map.Entry<String, ByteBuffer> entry : mapDataType.entrySet())
		{
			switch (entry.getKey())
			{
				case COMMAND_DATA_KEY:	// CMD
				{
					Map<String, String> mapEntityVal = DataFormatCenter.getEntityData(entry.getValue()); // pass the buffer
					
					for (Map.Entry<String, String> entryEntity : mapEntityVal.entrySet())
					{
						String key = entryEntity.getKey();
						String val = entryEntity.getValue();
						
						switch (key)
						{
							// Add command add here to receive request from client to do work
							// case XXXXXXX:
							// break;
								
							default:
								break;	
						}
					}
			
					break;
				}
				
				case MAP_DATA_KEY: // MAP
				{
					Map<String, String> mapEntityVal = DataFormatCenter.getEntityData(entry.getValue()); // pass the buffer
					
					for (Map.Entry<String, String> entryEntity : mapEntityVal.entrySet())
					{
						String key = entryEntity.getKey();
						String val = entryEntity.getValue();
						
						switch (key)
						{
							case ENTITY_KEY_REQUEST:
							{
								SendMazeMapData(session);
								break;
							}
								
							default:
								break;	
						}
					}
			
					break;
				}
			
				default:
				case PLAYER_DATA_KEY:	// PLY - received player info
				{
					ByteBuffer dataPlayer = entry.getValue();
					
					// Store this session client last data
					mapPlayer.get(session.getId()).setPlayerLastData(dataPlayer);
			    	
			    	// Do broadcast first
					for (PlayerHandler playerObj : mapPlayer.values())
			        {
						Session s = playerObj.getSession();
			            if (s.isOpen() && !s.equals(session)) 
			            {
			                try {
								 //Send By Binary Format
								 //data format in byte - "PLY dataSize Player1 id x y speed isWin isDie"
			                    s.getBasicRemote().sendBinary(dataPlayer);
			                } catch (IOException e) {
			                    e.printStackTrace();
			                }
			            }
			        }
					
			        Player player = DataFormatCenter.getOtherPlayerData(dataPlayer);
			        
			        CheckAndBroadcastGameEnd(player);
			        
			        // Then log for server display
			    	StringBuilder builder = new StringBuilder();
			    	builder.append(player.getName()).append(", ")
			    		   .append(player.getX()).append(", ")
			    		   .append(player.getY()).append(", ")
			    		   .append(player.getSpeed()).append(", ")
			    		   .append(player.getIsWin()).append(", ")
			    		   .append(player.getIsDie()).append(", ")
			    		   .append(player.getCurDir());
			    	
			    	System.out.println(builder.toString());
					break;
				}
			}
		}
    }
    
    @OnClose
    public void onClose(Session session) {
    	
    	String sessionID = session.getId();
    	
    	if (mapPlayer.containsKey(sessionID))
    	{
    		availablePlayerList.add(mapPlayer.get(sessionID).getePlayerID().ordinal()); // add back the available id back to the pool
    		mapPlayer.remove(sessionID);
    	}
        System.out.println("Connection closed, Client ID : " + session.getId());
    }
    
    private void SendMazeMapData(Session s)
    {
    	if (mapPlayer.containsKey(s.getId()))
    	{
    		PlayerHandler player = mapPlayer.get(s.getId());
    		
    		MazeData maze = mazeManager.getMazeData(mazeManager.getCurGameMapNum(), player.getePlayerID());
    		
    		ByteBuffer data = DataFormatCenter.createMazeMapByteBufferData(maze);
    		
    		if (data != null)
    		{
    			try {
					s.getBasicRemote().sendBinary(data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    
    private void CheckAndBroadcastGameEnd(Player player)
    {
    	if (player.getIsWin() == 1)
    	{
    		int playerId = player.getID().ordinal();
    		Map<String, String> mapRequest = Map.of(ENTITY_KEY_ID, String.valueOf(playerId));
    		ByteBuffer data = DataFormatCenter.createKeyByteBufferData(GAME_END_KEY, mapRequest);
    		
    		// Broadcast to all players
    		for (PlayerHandler playerObj : mapPlayer.values())
	        {
				Session s = playerObj.getSession();
	            if (s.isOpen()) 
	            {
	                try {
						 //Send By Binary Format
	                    s.getBasicRemote().sendBinary(data);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
    	}
    }
}
