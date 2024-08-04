package game.Maze;

import static game.UtilityAndConstant.ConstantAndDefine.GAME_BOARD_COLUMN;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_BOARD_ROW;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.*;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_TILE_SIZE;
import static game.UtilityAndConstant.ConstantAndDefine.eMAP.*;
import static game.UtilityAndConstant.ConstantAndDefine.eMAP;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import game.UtilityAndConstant.ConstantAndDefine.eMAP;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;

public class ServerMazeManager {
		
	private Map<eMAP, Maze> mapMazeObj;
	
	private eMAP curGameMapNum;
	
	public ServerMazeManager()
	{
		mapMazeObj = new HashMap<>();
		LoadAndCreateMaze();
		setCurGameMapNum(eMAP_1);
	}
	
	public void setCurGameMapNum(eMAP mapNum)
	{
		this.curGameMapNum = mapNum;
	}
	public eMAP getCurGameMapNum()
	{
		return this.curGameMapNum;
	}
	
	private void InitPlayerAtMapStartEndPt(Maze maze)
	{
		// Map 1, all star at top left corner
		// all end at last 2nd row idx and last column idx (start idx - 0)
		int endX = (GAME_BOARD_COLUMN - 1) * GAME_TILE_SIZE;		 // for 23 x 23, end (21, 22) idx row col
		int endY = (GAME_BOARD_ROW - 1 - 1) * GAME_TILE_SIZE;
		Point endPt = new Point(endX, endY);
		maze.setPlayerAtMapStartEndPt(eP1, new Point(0, 0), endPt);
		maze.setPlayerAtMapStartEndPt(eP2, new Point(0 + GAME_TILE_SIZE, 0), endPt);
		maze.setPlayerAtMapStartEndPt(eP3, new Point(0, 0 + GAME_TILE_SIZE), endPt);
		maze.setPlayerAtMapStartEndPt(eP4, new Point(0 + GAME_TILE_SIZE, 0 + GAME_TILE_SIZE), endPt);
		 
		// Map 2 here //
		 
	}
	
	private Boolean isResourcesExist(String resPath)
	{
		//return getClass().getClassLoader().getResource(resPath) != null;
		Path path = Paths.get("res/" + resPath);
		return Files.exists(path);
	}
	
	private void LoadAndCreateMaze()
	{
		// Create a Maze Object
		Maze maze = new Maze();
			
		for (int i = 0; i < eTOTAL_MAP.ordinal(); i++)
		{
			String resPath = String.format("/maps/maze%d.txt", i + 1);
			if (isResourcesExist(resPath))
			{
				eMAP mapNum = eMAP.fromValue(i);
				
				eTILE[][] arrTile = new eTILE[GAME_BOARD_ROW][GAME_BOARD_COLUMN];
				
				try 
				{
					InputStream is = getClass().getResourceAsStream(resPath);
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					
					int column = 0;
					int row = 0;
					
					while (column < GAME_BOARD_COLUMN &&
						   row < GAME_BOARD_ROW)
					{
						String eachRow = reader.readLine().trim();
						String[] eachTileNum = eachRow.split(" ");
						
						while (column < GAME_BOARD_COLUMN && 
								column < eachTileNum.length)
						{
							arrTile[row][column] = eTILE.fromValue(eachTileNum[column]);
							column++;
						}
						row++;
						column = 0;
					}
					
					// Store the mazeMap into obj,
					// Config the start end pt
					// put MapNum with corresponding Maze obj into mapMazeObj
					maze.setMapMaze(arrTile);
					InitPlayerAtMapStartEndPt(maze);
					
					mapMazeObj.put(mapNum, maze);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}
	
	public MazeData getMazeData(eMAP mapNum, ePLAYER eplayer)
	{
		if (mapMazeObj != null &&
			mapMazeObj.containsKey(mapNum))
		{
			Maze maze = mapMazeObj.get(mapNum);
			
			Point startPt = maze.getPlayerAtMapStartPt(eplayer);
			Point endPt = maze.getPlayerAtMapEndPt(eplayer);
			
			if (startPt == null)
			{
				startPt = new Point(0, 0);
			}
			
			if (endPt == null)
			{
				endPt = new Point(GAME_BOARD_ROW - 1, GAME_BOARD_COLUMN - 1);
			}
			
			return new MazeData(maze.getMapMaze(), startPt, endPt);
		}
		return null;
	}
}
