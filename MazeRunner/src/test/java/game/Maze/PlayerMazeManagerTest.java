package game.Maze;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.Player.Player;
import game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;

import static game.UtilityAndConstant.ConstantAndDefine.eTILE.ePATH;
import static game.UtilityAndConstant.ConstantAndDefine.eTILE.eTREE;
import static game.UtilityAndConstant.ConstantAndDefine.eTILE.eWALL;
import static game.UtilityAndConstant.ConstantAndDefine.eTILE.eFLAG;

class PlayerMazeManagerTest {

	private static PlayerMazeManager manager;
    private static eTILE[][] mazeTiles;
    private static Player player;
    
    @BeforeAll
    public static void setUp() {
        manager = new PlayerMazeManager();

        // Initialize maze tiles
        mazeTiles = new eTILE[][] {
            {ePATH, eWALL},
            {eTREE, eFLAG}
        };
        
        // Initialize player with mock values
        //player = new Player(ePLAYER.eP1, "Player1", new Point(0, 0), new Point(1, 1), 5, 0, 0, eCONTROL_KEY.eDOWN.ordinal());
        player = new Player(ePLAYER.eP1);
        player.setStartPos(new Point(0, 0));
        player.setFinishPos(new Point(1, 1));
    }
    
    @Test
    public void testConstructor() {
    	
    	manager = new PlayerMazeManager();
    	
        // Verify that mapTileTypeAttrib is correctly initialized
        Map<eTILE, TileEntity> expectedAttributes = new HashMap<>();
        expectedAttributes.put(ePATH, new TileEntity(false, false));
        expectedAttributes.put(eWALL, new TileEntity(true, false));
        expectedAttributes.put(eTREE, new TileEntity(true, true));
        expectedAttributes.put(eFLAG, new TileEntity(false, true));

        assertTrue(manager.getMapTileAttrib().containsKey(ePATH));
        assertTrue(manager.getMapTileAttrib().containsKey(eWALL));
        assertTrue(manager.getMapTileAttrib().containsKey(eTREE));
        assertTrue(manager.getMapTileAttrib().containsKey(eFLAG));
        
        assertFalse(manager.getMapTileAttrib().get(ePATH).isColliderOn());
        assertTrue(manager.getMapTileAttrib().get(eWALL).isColliderOn());
        assertTrue(manager.getMapTileAttrib().get(eTREE).isColliderOn());
        assertFalse(manager.getMapTileAttrib().get(eFLAG).isColliderOn());
    }

    @Test
    public void testSetMazeTileMap() {
        manager.setMazeTileMap(mazeTiles);
        assertArrayEquals("Maze tile map should be set correctly", mazeTiles, manager.getCurMazeTile());
    }
    
    @Test
    public void testCollisionCheckAndMove() {
        // Set maze tiles where collisions are expected
        eTILE[][] collisionMazeTiles = new eTILE[][] {
            {ePATH, ePATH},
            {eTREE, eFLAG}
        };
        manager.setMazeTileMap(collisionMazeTiles);
        
        // Test moving in different directions
        player.setSpeed(10); // example speed value

        boolean result = manager.CollisionCheckAndMove(player, eCONTROL_KEY.eUP);
        assertTrue("Collision should be detected when moving up", result);

        result = manager.CollisionCheckAndMove(player, eCONTROL_KEY.eDOWN);
        assertTrue("Collision should be detected when moving down", result);

        result = manager.CollisionCheckAndMove(player, eCONTROL_KEY.eLEFT);
        assertTrue("Collision should be detected when moving left", result);

        result = manager.CollisionCheckAndMove(player, eCONTROL_KEY.eRIGHT);
        assertFalse("No collision should be detected when moving right", result);
    }
    
    @Test
    public void testCheckIfPlayerWin() {
        // Set player finish position
        player.setFinishPos(new Point(48, 48)); // Assuming this position matches the finish point for win

        // Mock player's collider to be at the finish position
        player.setX(48);
        player.setY(48);

        manager.CheckIfPlayerWin(player);
        assertEquals("Player should win", 1, player.getIsWin());
    }
}
