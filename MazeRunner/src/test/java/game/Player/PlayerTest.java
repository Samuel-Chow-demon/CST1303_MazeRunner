package game.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.awt.Rectangle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;

import static game.UtilityAndConstant.ConstantAndDefine.INIT_X_SHIFT;
import static game.UtilityAndConstant.ConstantAndDefine.INIT_Y_SHIFT;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_DEFAULT_SPEED;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_SIZE_XY;
import static game.UtilityAndConstant.ConstantAndDefine.UI_HEIGHT;
import static game.UtilityAndConstant.ConstantAndDefine.UI_WIDTH;

class PlayerTest {

	private static Player player1;
    private static Player player2;
    
    @BeforeAll
    public static void setUp() {
        player1 = new Player(ePLAYER.eP1);
        player2 = new Player(ePLAYER.eP2, "Player 2", new Point(100, 100), new Point(200, 200), 10, 0, 0, eCONTROL_KEY.eUP.ordinal());
    }
    
    @Test
    public void testDefaultConstructor() {
    	player1 = new Player(ePLAYER.eP1);
        assertEquals(new Point(INIT_X_SHIFT, INIT_Y_SHIFT), player1.getPos());
        assertEquals(PLAYER_DEFAULT_SPEED, player1.getSpeed());
        //String player1Name = player1.getName();
        assertEquals("Player1", player1.getName());
        assertEquals(eCONTROL_KEY.eDOWN, player1.getCurDir());
        assertEquals(0, player1.getIsWin());
        assertEquals(0, player1.getIsDie());
    }
    
    @Test
    public void testParameterizedConstructor() {
        assertEquals("Player 2", player2.getName());
        assertEquals(new Point(100, 100), player2.getPos());
        assertEquals(new Point(200, 200), player2.getFinishPos());
        int val = player2.getSpeed();
        assertEquals(10, player2.getSpeed());
        assertEquals(eCONTROL_KEY.eUP, player2.getCurDir());
        assertEquals(0, player2.getIsWin());
        assertEquals(0, player2.getIsDie());
    }
    
    @Test
    public void testCopyConstructor() {
        Player copyPlayer = new Player(player2);
        assertEquals(player2.getID(), copyPlayer.getID());
        assertEquals(player2.getName(), copyPlayer.getName());
        assertEquals(player2.getPos(), copyPlayer.getPos());
        assertEquals(player2.getFinishPos(), copyPlayer.getFinishPos());
        assertEquals(player2.getSpeed(), copyPlayer.getSpeed());
        assertEquals(player2.getCurDir(), copyPlayer.getCurDir());
    }
    
    @Test
    public void testResetAttributes() {
        player2.resetAttributes();
        assertEquals(new Point(100, 100), player2.getPos());
        assertEquals(PLAYER_DEFAULT_SPEED, player2.getSpeed());
        assertEquals(eCONTROL_KEY.eDOWN, player2.getCurDir());
        assertEquals(0, player2.getIsWin());
        assertEquals(0, player2.getIsDie());
    }
    
    @Test
    public void testSettersAndGetters() {
        player1.setSpeed(15);
        assertEquals(15, player1.getSpeed());

        player1.setWin();
        assertEquals(1, player1.getIsWin());

        player1.setDie();
        assertEquals(1, player1.getIsDie());

        player1.setX(200);
        assertEquals(200, player1.getX());

        player1.setY(300);
        assertEquals(300, player1.getY());

        player1.setFinishPos(new Point(500, 500));
        assertEquals(new Point(500, 500), player1.getFinishPos());

        player1.setDirection(eCONTROL_KEY.eLEFT);
        assertEquals(eCONTROL_KEY.eLEFT, player1.getCurDir());
    }

    @Test
    public void testMovement() {
    	player1 = new Player(ePLAYER.eP1);
    	
        player1.setSpeed(5);

        player1.moveUp();
        assertEquals(new Point(INIT_X_SHIFT, INIT_Y_SHIFT - 5), player1.getPos());

        player1.moveDown();
        assertEquals(new Point(INIT_X_SHIFT, INIT_Y_SHIFT), player1.getPos());

        player1.moveLeft();
        assertEquals(new Point(INIT_X_SHIFT - 5, INIT_Y_SHIFT), player1.getPos());

        player1.moveRight();
        assertEquals(new Point(INIT_X_SHIFT, INIT_Y_SHIFT), player1.getPos());

        // Boundary conditions
        player1.setX(0);
        assertTrue(player1.moveLeft());

        player1.setX(UI_WIDTH - PLAYER_SIZE_XY);
        assertTrue(player1.moveRight());

        player1.setY(0);
        assertTrue(player1.moveUp());

        player1.setY(UI_HEIGHT - PLAYER_SIZE_XY);
        assertTrue(player1.moveDown());
    }
    
    @Test
    public void testSetStartPos() 
    {
        // Define new start position
        Point newStartPos = new Point(150, 250);

        // Set the new start position
        player1.setStartPos(newStartPos);

        // Verify that position has also been updated correctly
        assertEquals(newStartPos, player1.getPos());
    }

    @Test
    public void testCollider() {
        Rectangle collider = player1.getCollider();
        assertEquals(new Rectangle(PLAYER_SIZE_XY / 6, PLAYER_SIZE_XY / 3, PLAYER_SIZE_XY - ((PLAYER_SIZE_XY / 6) * 2), PLAYER_SIZE_XY - (PLAYER_SIZE_XY / 2)), collider);
    }

}
