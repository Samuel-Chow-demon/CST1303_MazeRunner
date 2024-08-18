package game.Maze;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.UtilityAndConstant.ConstantAndDefine.eMAP;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_BOARD_COLUMN;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_BOARD_ROW;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_TILE_SIZE;

class ServerMazeManagerTest {

	private ServerMazeManager serverMazeManager;
	
	@BeforeEach
    void setUp() {
        serverMazeManager = new ServerMazeManager();
    }

	@Test
    void testConstructor() {
        assertNotNull(serverMazeManager);
        assertEquals(eMAP.eMAP_1, serverMazeManager.getCurGameMapNum(), "Initial map should be eMAP_1");
    }
	
	@Test
    void testSetAndGetCurGameMapNum() {
        serverMazeManager.setCurGameMapNum(eMAP.eMAP_2);
        assertEquals(eMAP.eMAP_2, serverMazeManager.getCurGameMapNum(), "Map should be set to eMAP_2");
    }
    
    @Test
    void testGetMazeData() {
        MazeData mazeData = serverMazeManager.getMazeData(eMAP.eMAP_1, ePLAYER.eP1);
        assertNotNull(mazeData, "MazeData should not be null");
        
        Point expectedStart = new Point(0, 0);
        Point expectedEnd = new Point((GAME_BOARD_COLUMN - 1) * GAME_TILE_SIZE, 
                                      (GAME_BOARD_ROW - 1 - 1) * GAME_TILE_SIZE);
        assertEquals(expectedStart, mazeData.getStartPt(), "Start point should match the expected value");
        assertEquals(expectedEnd, mazeData.getEndPt(), "End point should match the expected value");
    }
    
    @Test
    void testGetMazeDataInvalidMap() {
        MazeData mazeData = serverMazeManager.getMazeData(eMAP.eMAP_2, ePLAYER.eP1); // Assuming eMAP_3 doesn't exist
        assertNull(mazeData, "MazeData should be null for an invalid map");
    }
}
