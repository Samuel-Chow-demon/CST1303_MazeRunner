package game.UtilityAndConstant;

import static game.UtilityAndConstant.ConstantAndDefine.COMMAND_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.DATA_KEY_BYTE_SIZE;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_KEY_MAX_SIZE;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_VAL_MAX_SIZE;
import static game.UtilityAndConstant.ConstantAndDefine.MAP_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_NAME_MAX_SIZE;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import game.Maze.MazeData;
import game.Player.Player;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;

public class DataFormatCenter {
	
	public static ByteBuffer createPlayerByteBufferData(Player player)
	{
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
	        
	        // Stream Key - PLY
	        byte[] keyBytes = new byte[DATA_KEY_BYTE_SIZE];
	        System.arraycopy(PLAYER_DATA_KEY.getBytes(), 0, keyBytes, 0, DATA_KEY_BYTE_SIZE);
	        
	        // Data - Start
	        byte[] nameBytes = new byte[PLAYER_NAME_MAX_SIZE];
	        System.arraycopy(player.getName().getBytes(), 0, nameBytes, 0, Math.min(player.getName().length(), PLAYER_NAME_MAX_SIZE));
	        
	        int[] valArray = {
	        				player.getID().ordinal(),
        					player.getX(),
        					player.getY(),
        					player.getFinishPos().x,
        					player.getFinishPos().y,
        					player.getSpeed(),
        					player.getIsWin(),
        					player.getIsDie(),
        					player.getCurDir().ordinal()
        					// new data integer value insert here
        					};
	        
	        
	        // get rid of Key and this byteSizeValue byte
	        int dataByteSize = PLAYER_NAME_MAX_SIZE + (valArray.length * Integer.BYTES);
	        
	        // data format - "PLY UsefulDataByteSize Player1 id x y finX finY speed isWin isDie curDir" // start with PLY - Player data key
	        dataOutputStream.write(keyBytes);
	        dataOutputStream.writeInt(dataByteSize);
	        
	        // Useful data
			dataOutputStream.write(nameBytes);
			for(int val : valArray)
			{
				dataOutputStream.writeInt(val);
			}
			
	        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// extract data back as Player object from byte binary
	public static Player getOtherPlayerData(DataInputStream dataInputStream)
	{
		try {
			
			// Read and shift the stream pos of Cmd and ByteSizeInteger
			// PLY XX
			byte[] cmdBytes = new byte[DATA_KEY_BYTE_SIZE];
			dataInputStream.readFully(cmdBytes);
			String cmd = new String(cmdBytes).trim();
			
			int dataSize = dataInputStream.readInt();
	        
			// data format - "PLY UsefulDataByteSize Player1 id x y finX finY speed isWin isDie curDir" // start with PLY - Player data key
	        byte[] nameBytes = new byte[PLAYER_NAME_MAX_SIZE];
	        dataInputStream.readFully(nameBytes);
	        String playerName = new String(nameBytes).trim();
	        ePLAYER id = ePLAYER.fromValue(dataInputStream.readInt());
	        int x = dataInputStream.readInt();
	        int y = dataInputStream.readInt();
	        int finX = dataInputStream.readInt();
	        int finY = dataInputStream.readInt();
	        int speed = dataInputStream.readInt();
	        int isWin = dataInputStream.readInt();
	        int isDie = dataInputStream.readInt();
	        int curDir = dataInputStream.readInt();
	        
	        return new Player(id, playerName, new Point(x, y), new Point(finX, finY), speed, isWin, isDie, curDir);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// extract data back as Player object from byte binary
	public static Player getOtherPlayerData(ByteBuffer data)
	{
		DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data.array()));
		
		return getOtherPlayerData(dataInputStream);
	}
	
	
	public static ByteBuffer createKeyByteBufferData(String keyItem, Map<String, String> mapEntityAndVal)
	{
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
	        
	        byte[] cmdBytes = new byte[DATA_KEY_BYTE_SIZE];
	        System.arraycopy(keyItem.getBytes(), 0, cmdBytes, 0, DATA_KEY_BYTE_SIZE);
	        dataOutputStream.write(cmdBytes);
	        
	        final int ONE_ENTRY_VAL_BYTE_SIZE = ENTITY_KEY_MAX_SIZE + ENTITY_VAL_MAX_SIZE;
	        
	        // get rid of Key and this byteSizeValue byte
	        int dataByteSize = mapEntityAndVal.size() * ONE_ENTRY_VAL_BYTE_SIZE;
	        dataOutputStream.writeInt(dataByteSize);
	        
	        for (Map.Entry<String, String> entry : mapEntityAndVal.entrySet())
	        {
	        	byte[] entityKeyBytes = new byte[ENTITY_KEY_MAX_SIZE];
		        byte[] valBytes = new byte[ENTITY_VAL_MAX_SIZE];
		        
		        String key = entry.getKey();
		        String val = entry.getValue();
	 	        
		        // Cmd format - "CMD EntityKey1 value1 EntityKey2 value2 ...." // start with CMD, then the key, with value after
		        System.arraycopy(key.getBytes(), 0, entityKeyBytes, 0, Math.min(key.length(), ENTITY_KEY_MAX_SIZE));
		        System.arraycopy(val.getBytes(), 0, valBytes, 0, Math.min(val.length(), ENTITY_VAL_MAX_SIZE));
		        
		        dataOutputStream.write(entityKeyBytes);
				dataOutputStream.write(valBytes);
	        }
	        	  
	        // Cmd format - "CMD UsefulDataByteSize EntityKey1 value1 EntityKey2 value2 ...." // start with CMD, usefuldatabytesze, then the key, with value after
	        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<String, String> getEntityData(DataInputStream dataInputStream)
	{
		try {
			Map<String, String> mapVal = new HashMap<>();
			
			// Read and shift the stream pos of Cmd and ByteSizeInteger
			byte[] cmdBytes = new byte[DATA_KEY_BYTE_SIZE];
			dataInputStream.readFully(cmdBytes);
			String cmd = new String(cmdBytes).trim();
			
			int dataSize = dataInputStream.readInt();
			
			// only useful data in the stream, no CMD and bytesize data
			while (dataInputStream.available() >= (ENTITY_KEY_MAX_SIZE + ENTITY_VAL_MAX_SIZE))
			{
				byte[] keyBytes = new byte[ENTITY_KEY_MAX_SIZE];
		        byte[] valBytes = new byte[ENTITY_VAL_MAX_SIZE];
		        dataInputStream.readFully(keyBytes);
		        dataInputStream.readFully(valBytes);
		        
		        String key = new String(keyBytes, StandardCharsets.UTF_8).trim();
		        String val = new String(valBytes, StandardCharsets.UTF_8).trim();
		        
		        mapVal.put(key, val);
			}
			   
	        return mapVal;
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<String, String> getEntityData(ByteBuffer data)
	{
		DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data.array()));
		
		return getEntityData(dataInputStream);
	}
	
	// Return [Data_Type_Key, DataInputStream]
	// Separate the CMD or PLY data with their corresponding byte into different stream
	// Since the sendBinary websocket might combine all the data to trigger onMessage by one time only
	public static Map<String, ByteBuffer> getDataStream(ByteBuffer data)
	{
		Map<String, ByteBuffer> mapDiffTypeDataStream = new HashMap<>();
		
		final int eachDataTypeLeastByte = DATA_KEY_BYTE_SIZE + Integer.BYTES + 1;
		
		int startPosition = 0;
		while (startPosition + eachDataTypeLeastByte < data.limit())
		{
			// "CMD" , "PLY"
			byte[] dataType = new byte[DATA_KEY_BYTE_SIZE];
			
			data.get(dataType, startPosition, startPosition + DATA_KEY_BYTE_SIZE);
			String cmdtype = new String(dataType).trim();
			
			// Then get the later on useful byte size
			int usefulDataByteSize = data.getInt();
			
			// The Key byte + data size integerbyte + later one byte size [for each command data stream]
			int extractTotalBytesEachCmd = DATA_KEY_BYTE_SIZE + Integer.BYTES + usefulDataByteSize;
			
			// Check if the later on extract data still within the buffer
			if (startPosition + extractTotalBytesEachCmd <= data.limit())
			{
				data.position(startPosition);
				data.limit(startPosition + extractTotalBytesEachCmd);
				ByteBuffer curCmdDataSliceBuffer = data.slice();
				
//				byte[] byteArray = new byte[curCmdDataSlice.remaining()];
//				curCmdDataSliceBuffer.get(byteArray);
//				String dataSlice = new String(byteArray);
				
				mapDiffTypeDataStream.put(cmdtype, curCmdDataSliceBuffer);
				
				// Restore original limit
				data.limit(data.capacity());
				// Shift the start for next cmd
				startPosition += usefulDataByteSize;
			}
			else
			{
				break;
			}
		
			// remark
			// Below is if the slice is not to include the Cmd and dataByte info
			// Check if the later on extract data still within the buffer
//			if (data.position() + usefulDataByteSize <= data.limit())
//			{
//				int pos = data.position();
//				
//				data.limit(pos + usefulDataByteSize);
//				ByteBuffer usefulDataSlice = data.slice();
//				
//				byte[] byteArray = new byte[usefulDataSlice.remaining()];
//				usefulDataSlice.get(byteArray);
//				String dataSlice = new String(byteArray);
//				
//				// Restore original limit
//				data.limit(data.capacity());
//				
//				mapDiffTypeDataStream.put(new String(dataType), new DataInputStream(new ByteArrayInputStream(byteArray)));
//				
//				startPosition = data.position() + usefulDataByteSize;
//			}
//			else
//			{
//				break;
//			}
		}
		
		return mapDiffTypeDataStream;
	}
	
	
	public static ByteBuffer createMazeMapByteBufferData(MazeData mazeData)
	{
		eTILE[][] arrTile = mazeData.getMapTile();
		
		if (arrTile == null ||
			arrTile.length <= 0 ||
			arrTile[0].length <= 0) // at least have one row data
		{
			return null;
		}
		
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
	        
	        Point startPt = mazeData.getStartPt();
	        Point endPt = mazeData.getEndPt();
	        
	        // Stream Key - MAP
	        byte[] keyBytes = new byte[DATA_KEY_BYTE_SIZE];
	        System.arraycopy(MAP_DATA_KEY.getBytes(), 0, keyBytes, 0, DATA_KEY_BYTE_SIZE);
	        
	        // Data - Start,  data format - row, col, startx, starty, endx, endy, mapdata(11111100000011111......)
	        int row = arrTile.length;
	        int col = arrTile[0].length; // Map data array must be in square matrix
	        
	        int[] cfgArray = {
	        		row,
	        		col,
	        		startPt.x,
	        		startPt.y,
	        		endPt.x,
	        		endPt.y
					// new data integer value insert here
					};
	             
	        // get rid of Key and this byteSizeValue byte
	        int dataByteSize = (cfgArray.length * Integer.BYTES) + (row * col * Integer.BYTES);
	        
	        // Total stream data
	        // - "MAP, UsefulDataByteSize, row, col, startx, starty, endx, endy, mapdata(11111100000011111......)"
	        dataOutputStream.write(keyBytes);
	        dataOutputStream.writeInt(dataByteSize);
	        
	        // ------------- Useful data
	        // config data
			for(int val : cfgArray)
			{
				dataOutputStream.writeInt(val);
			}
			// map data
			for (eTILE[] valEachRow : arrTile)
			{
				for (eTILE val : valEachRow)
				{
					dataOutputStream.writeInt(val.ordinal());
				}
			}
			
	        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// extract data back as eTILE[][] from byte binary map data
	public static MazeData getMazeMapData(DataInputStream dataInputStream)
	{
		try {
			
			// Read and shift the stream pos of Cmd and ByteSizeInteger
			// MAP XX
			byte[] cmdBytes = new byte[DATA_KEY_BYTE_SIZE];
			dataInputStream.readFully(cmdBytes);
			String cmd = new String(cmdBytes).trim();
			
			int dataSize = dataInputStream.readInt();
	        
			// data format - "MAP, UsefulDataByteSize, row, col, startx, starty, endx, endy, mapdata(11111100000011111......)"
			int row = dataInputStream.readInt();
		    int col = dataInputStream.readInt();
	        int startX = dataInputStream.readInt();
	        int startY = dataInputStream.readInt();
	        int endX = dataInputStream.readInt();
	        int endY = dataInputStream.readInt();
	        
	        eTILE[][] tileMap = new eTILE[row][col];
	        
	        for (int i = 0; i < row; i++)
	        {
	        	for (int j = 0; j < col; j++)
	        	{
	        		tileMap[i][j] = eTILE.fromValue(dataInputStream.readInt());
	        	}
	        }
	       
	        return new MazeData(tileMap, new Point(startX, startY), new Point(endX, endY));
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static MazeData getMazeMapData(ByteBuffer data)
	{
		DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data.array()));
		
		return getMazeMapData(dataInputStream);
	}

}
