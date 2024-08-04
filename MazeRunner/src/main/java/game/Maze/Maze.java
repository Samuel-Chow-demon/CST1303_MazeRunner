package game.Maze;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;

public class Maze {
	
	private eTILE[][] mapMaze;
	Map<ePLAYER, Point> mapPlayerAtMapStartPt;
	Map<ePLAYER, Point> mapPlayerAtMapEndPt;
	
	public Maze()
	{
		mapMaze = null;
		mapPlayerAtMapStartPt = new HashMap<>();
		mapPlayerAtMapEndPt = new HashMap<>();
	}
	

	public eTILE[][] getMapMaze() {
		return mapMaze;
	}


	public void setMapMaze(eTILE[][] mapMaze) {
		this.mapMaze = mapMaze;
	}


	public Point getPlayerAtMapStartPt(ePLAYER player) 
	{
		if (mapPlayerAtMapStartPt.containsKey(player))
		{
			return mapPlayerAtMapStartPt.get(player);
		}
		return null;
	}

	public Point getPlayerAtMapEndPt(ePLAYER player) 
	{
		if (mapPlayerAtMapEndPt.containsKey(player))
		{
			return mapPlayerAtMapEndPt.get(player);
		}
		return null;
	}

	public void setPlayerAtMapStartEndPt(ePLAYER player, Point startPt, Point endPt)
	{
		this.mapPlayerAtMapStartPt.put(player, startPt);
		this.mapPlayerAtMapEndPt.put(player, endPt);
	}
}
