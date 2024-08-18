package game.Player;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eTOTAL_KEY;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;

class InputKeyHandlerTest {

	private static InputKeyHandler keyHandler;
	
	@BeforeAll
    public static void setUp() {
        keyHandler = new InputKeyHandler(ePLAYER.eP1); // Initialize with a specific player ID
    }
	
	@Test
    public void testInitialization() {
        // Test if all keys are initially not pressed
        for (eCONTROL_KEY key : eCONTROL_KEY.values()) {
        	if (key != eTOTAL_KEY)
        	{        		
        		assertFalse("Key " + key + " should not be pressed initially", keyHandler.isKeyPressed(key));
        	}
        }
    }
	
	@Test
    public void testKeyPressed() {
        // Simulate key press
        KeyEvent upKeyEvent = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, ' ');
        keyHandler.keyPressed(upKeyEvent);

        // Verify that the UP key is marked as pressed
        assertTrue(keyHandler.isKeyPressed(eCONTROL_KEY.eUP));
    }
	
	@Test
    public void testKeyReleased() {
        // Simulate key press and release
        KeyEvent upKeyEvent = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, ' ');
        keyHandler.keyPressed(upKeyEvent);
        keyHandler.keyReleased(upKeyEvent);

        // Verify that the UP key is marked as not pressed
        assertFalse(keyHandler.isKeyPressed(eCONTROL_KEY.eUP));
    }
	
	@Test
    public void testGetPlayerControlDescription() {
        // Test description for eP1
        String description = keyHandler.getPlayerControlDescription(ePLAYER.eP1);
        assertEquals("(Arrow : UP  DOWN  LEFT  RIGHT)", description);

        // Initialize with a different player ID
        keyHandler = new InputKeyHandler(ePLAYER.eP2);
        description = keyHandler.getPlayerControlDescription(ePLAYER.eP2);
        assertEquals("(UP-W  DOWN-S  LEFT-A  RIGHT-D)", description);
    }
}
