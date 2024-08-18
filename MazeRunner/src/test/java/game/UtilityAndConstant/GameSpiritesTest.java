package game.UtilityAndConstant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;

class GameSpiritesTest {

	static private GameSpirites gameSpirites;

    @BeforeAll
    public static void setUp() {
        gameSpirites = GameSpirites.getInstance();
    }

    @Test
    public void testPlayerColor() {
        assertEquals(Color.BLUE, GameSpirites.PlayerColor(ePLAYER.eP1));
        assertEquals(Color.GREEN, GameSpirites.PlayerColor(ePLAYER.eP2));
        assertEquals(Color.MAGENTA, GameSpirites.PlayerColor(ePLAYER.eP3));
        assertEquals(Color.ORANGE, GameSpirites.PlayerColor(ePLAYER.eP4));
    }

    @Test
    public void testGetPlayerImage() {
        Map<ePLAYER_SPRITE_DIR, BufferedImage> playerImage = gameSpirites.getPlayerImage(ePLAYER.eP1);
        assertNotNull(playerImage);
        assertEquals(8, playerImage.size()); // 8 directions
    }

    @Test
    public void testGetMazeTileImage() {
        BufferedImage tileImage = gameSpirites.getMazeTileImage(eTILE.ePATH);
        assertNotNull(tileImage);
    }

    @Test
    public void testTileColor() {
        assertEquals(Color.GRAY, GameSpirites.TileColor(eTILE.ePATH));
        assertEquals(Color.decode("#825118"), GameSpirites.TileColor(eTILE.eWALL));
    }

    @Test
    public void testGetScaledImage() {
        BufferedImage originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        BufferedImage scaledImage = gameSpirites.getScaledImage(originalImage, 50, 50);
        assertNotNull(scaledImage);
        assertEquals(50, scaledImage.getWidth());
        assertEquals(50, scaledImage.getHeight());
    }

    @Test
    public void testGetPlayerBufferedImage() {
        Map<ePLAYER_SPRITE_DIR, BufferedImage> playerImage = gameSpirites.getPlayerBufferedImage(ePLAYER.eP1);
        assertNotNull(playerImage);
        assertEquals(8, playerImage.size()); // 8 directions
    }

    @Test
    public void testGetTileEntityBufferedImage() {
        BufferedImage tileImage = gameSpirites.getTileEntityBufferedImage(eTILE.ePATH);
        assertNotNull(tileImage);
    }

}
