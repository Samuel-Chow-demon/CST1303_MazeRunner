package game.UtilityAndConstant;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import game.Maze.MazeData;
import game.Player.Player;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;
import game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;

class DataFormatCenterTest {

	// Sample PLAYER constants for testing
    private static final String TEST_PLAYER_NAME = "TestPlayer";
    private static final ePLAYER TEST_PLAYER_ID = ePLAYER.eP1;
    private static final Point TEST_START_POS = new Point(10, 20);
    private static final Point TEST_END_POS = new Point(30, 40);
    private static final int TEST_SPEED = 5;
    private static final int TEST_IS_WIN = 1;
    private static final int TEST_IS_DIE = 0;
    private static final int TEST_CUR_DIR = eCONTROL_KEY.eRIGHT.ordinal();

    @Test
    public void testCreatePlayerByteBufferDataAndGetOtherPlayerData() {
        // Create a sample Player object
        Player player = new Player(TEST_PLAYER_ID, TEST_PLAYER_NAME, TEST_START_POS, TEST_END_POS, TEST_SPEED, TEST_IS_WIN, TEST_IS_DIE, TEST_CUR_DIR);

        // Serialize the Player object
        ByteBuffer playerBuffer = DataFormatCenter.createPlayerByteBufferData(player);

        // Deserialize the ByteBuffer back to a Player object
        Player deserializedPlayer = DataFormatCenter.getOtherPlayerData(playerBuffer);

        // Verify that the original and deserialized Player objects are equivalent
        assertNotNull(deserializedPlayer);
        assertEquals(player.getID(), deserializedPlayer.getID());
        assertEquals(player.getName(), deserializedPlayer.getName());
        assertEquals(player.getX(), deserializedPlayer.getX());
        assertEquals(player.getY(), deserializedPlayer.getY());
        assertEquals(player.getFinishPos(), deserializedPlayer.getFinishPos());
        assertEquals(player.getSpeed(), deserializedPlayer.getSpeed());
        assertEquals(player.getIsWin(), deserializedPlayer.getIsWin());
        assertEquals(player.getIsDie(), deserializedPlayer.getIsDie());
        assertEquals(player.getCurDir(), deserializedPlayer.getCurDir());
    }

    @Test
    public void testCreateKeyByteBufferDataAndGetEntityData() {
        // Create a sample key-item map
        String keyItem = "CMD";
        Map<String, String> mapEntityAndVal = new HashMap<>();
        mapEntityAndVal.put("Entity1", "Value1");
        mapEntityAndVal.put("Entity2", "Value2");

        // Serialize the key-item map
        ByteBuffer keyBuffer = DataFormatCenter.createKeyByteBufferData(keyItem, mapEntityAndVal);

        // Deserialize the ByteBuffer back to a map
        Map<String, String> deserializedMap = DataFormatCenter.getEntityData(keyBuffer);

        // Verify that the original and deserialized maps are equivalent
        assertNotNull(deserializedMap);
        assertEquals(mapEntityAndVal.size(), deserializedMap.size());
        for (Map.Entry<String, String> entry : mapEntityAndVal.entrySet()) {
            assertTrue(deserializedMap.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), deserializedMap.get(entry.getKey()));
        }
    }

    @Test
    public void testCreateMazeMapByteBufferDataAndGetMazeMapData() {
        // Create a sample MazeData object
        eTILE[][] mapTile = {
            {eTILE.eWALL, eTILE.ePATH, eTILE.ePATH},
            {eTILE.ePATH, eTILE.eWALL, eTILE.ePATH},
            {eTILE.ePATH, eTILE.ePATH, eTILE.eFLAG}
        };
        Point startPt = new Point(0, 0);
        Point endPt = new Point(2, 2);
        MazeData mazeData = new MazeData(mapTile, startPt, endPt);

        // Serialize the MazeData object
        ByteBuffer mazeBuffer = DataFormatCenter.createMazeMapByteBufferData(mazeData);

        // Deserialize the ByteBuffer back to a MazeData object
        MazeData deserializedMazeData = DataFormatCenter.getMazeMapData(mazeBuffer);

        // Verify that the original and deserialized MazeData objects are equivalent
        assertNotNull(deserializedMazeData);
        assertArrayEquals(mazeData.getMapTile(), deserializedMazeData.getMapTile());
        assertEquals(mazeData.getStartPt(), deserializedMazeData.getStartPt());
        assertEquals(mazeData.getEndPt(), deserializedMazeData.getEndPt());
    }
    
    @Test
    public void testGetDataStream() {
    	
    	// Create a sample Player object
        Player player = new Player(TEST_PLAYER_ID, TEST_PLAYER_NAME, TEST_START_POS, TEST_END_POS, TEST_SPEED, TEST_IS_WIN, TEST_IS_DIE, TEST_CUR_DIR);

        // Serialize the Player object
        ByteBuffer playerBuffer = DataFormatCenter.createPlayerByteBufferData(player);
        
        // Call the method
        Map<String, ByteBuffer> result = DataFormatCenter.getDataStream(playerBuffer);
        
        
        // Verify the results
        assertEquals(1, result.size());
        
        assertTrue(result.containsKey("PLY"));
        
        ByteBuffer plyBuffer = result.get("PLY");
        
     // Deserialize the ByteBuffer back to a Player object
        Player deserializedPlayer = DataFormatCenter.getOtherPlayerData(plyBuffer);

        // Verify that the original and deserialized Player objects are equivalent
        assertNotNull(deserializedPlayer);
        assertEquals(player.getID(), deserializedPlayer.getID());
        assertEquals(player.getName(), deserializedPlayer.getName());
        assertEquals(player.getX(), deserializedPlayer.getX());
        assertEquals(player.getY(), deserializedPlayer.getY());
        assertEquals(player.getFinishPos(), deserializedPlayer.getFinishPos());
        assertEquals(player.getSpeed(), deserializedPlayer.getSpeed());
        assertEquals(player.getIsWin(), deserializedPlayer.getIsWin());
        assertEquals(player.getIsDie(), deserializedPlayer.getIsDie());
        assertEquals(player.getCurDir(), deserializedPlayer.getCurDir());
    	
    }

}
