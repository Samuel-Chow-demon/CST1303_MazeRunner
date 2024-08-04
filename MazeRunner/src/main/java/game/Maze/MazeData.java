package game.Maze;

import java.awt.Point;

import game.UtilityAndConstant.ConstantAndDefine.eTILE;

public class MazeData {
	eTILE[][] mapTile;
	Point startPt;
	Point endPt;
	
	public MazeData(eTILE[][] tile, Point startPt, Point endPt)
	{
		this.mapTile = tile;
		this.startPt = startPt;
		this.endPt = endPt;
	}

	public eTILE[][] getMapTile() {
		return mapTile;
	}

	public void setMapTile(eTILE[][] mapTile) {
		this.mapTile = mapTile;
	}

	public Point getStartPt() {
		return this.startPt;
	}

	public void setStartPt(Point startPt) {
		this.startPt = startPt;
	}
	
	public Point getEndPt() {
		return this.endPt;
	}

	public void setEndPt(Point endPt) {
		this.endPt = endPt;
	}
}
