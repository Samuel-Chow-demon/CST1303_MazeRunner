package game.Maze;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;

class MazeTest {

	private static Maze maze;

    @BeforeAll
    public static void setUp() {
        maze = new Maze();
    }

    @Test
    public void testInitialState() 
    {
    	maze = new Maze();
        // Test if the initial state is as expected
        assertNull("mapMaze should be null initially", maze.getMapMaze());
        assertNull("Player start point should be null initially", maze.getPlayerAtMapStartPt(ePLAYER.eP1));
        assertNull("Player end point should be null initially", maze.getPlayerAtMapEndPt(ePLAYER.eP1));
    }
    
    @Test
    public void testSetMapMaze() {
        // Create a sample map
        eTILE[][] sampleMap = {
            {eTILE.ePATH, eTILE.eWALL},
            {eTILE.eWALL, eTILE.ePATH}
        };
        
        maze.setMapMaze(sampleMap);
        assertArrayEquals("mapMaze should be updated correctly", sampleMap, maze.getMapMaze());
    }
    
    @Test
    public void testSetAndGetPlayerStartEndPoints() {
        // Define points
        Point startPoint = new Point(1, 1);
        Point endPoint = new Point(2, 2);
        
        // Set player start and end points
        maze.setPlayerAtMapStartEndPt(ePLAYER.eP1, startPoint, endPoint);

        // Get and verify player start and end points
        assertEquals("Player start point should be (1, 1)", startPoint, maze.getPlayerAtMapStartPt(ePLAYER.eP1));
        assertEquals("Player end point should be (2, 2)", endPoint, maze.getPlayerAtMapEndPt(ePLAYER.eP1));
    }
    
    @Test
    public void testPlayerStartAndEndPointsNotFound() {
        // Verify that non-existent player returns null for start and end points
        assertNull("Non-existent player start point should be null", maze.getPlayerAtMapStartPt(ePLAYER.eP2));
        assertNull("Non-existent player end point should be null", maze.getPlayerAtMapEndPt(ePLAYER.eP2));
    }

}
