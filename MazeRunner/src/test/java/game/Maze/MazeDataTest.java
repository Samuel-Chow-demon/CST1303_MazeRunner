package game.Maze;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.UtilityAndConstant.ConstantAndDefine.eTILE;

class MazeDataTest {

	private eTILE[][] mapTile;
    private Point startPt;
    private Point endPt;
    private MazeData mazeData;
    
    @Test
    public void testConstructor() {
    	mapTile = new eTILE[][] {
            {eTILE.ePATH, eTILE.eWALL},
            {eTILE.eWALL, eTILE.ePATH}
        };
        startPt = new Point(1, 1);
        endPt = new Point(2, 2);
        mazeData = new MazeData(mapTile, startPt, endPt);
        
        assertArrayEquals("Map tiles should be initialized correctly", mapTile, mazeData.getMapTile());
        assertEquals("Start point should be initialized correctly", startPt, mazeData.getStartPt());
        assertEquals("End point should be initialized correctly", endPt, mazeData.getEndPt());
    }
    
    @Test
    public void testSetMapTile() {
    	eTILE[][] newMapTile = new eTILE[][] {
            {eTILE.ePATH, eTILE.eWALL},
            {eTILE.eWALL, eTILE.ePATH}
        };
        startPt = new Point(1, 1);
        endPt = new Point(2, 2);
        mazeData = new MazeData(mapTile, startPt, endPt);
        
        mazeData.setMapTile(newMapTile);
        assertArrayEquals("Map tiles should be updated correctly", newMapTile, mazeData.getMapTile());
    }
    
    @Test
    public void testSetStartPt() {
    	startPt = new Point(1, 1);
        endPt = new Point(2, 2);
        mazeData = new MazeData(mapTile, startPt, endPt);
        
        Point newStartPt = new Point(3, 3);
        mazeData.setStartPt(newStartPt);
        assertEquals("Start point should be updated correctly", newStartPt, mazeData.getStartPt());
    }
    
    @Test
    public void testSetEndPt() {
    	startPt = new Point(1, 1);
        endPt = new Point(2, 2);
        mazeData = new MazeData(mapTile, startPt, endPt);
        
        Point newEndPt = new Point(4, 4);
        mazeData.setEndPt(newEndPt);
        assertEquals("End point should be updated correctly", newEndPt, mazeData.getEndPt());
    }

}
