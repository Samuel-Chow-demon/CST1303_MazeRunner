package game.Maze;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileEntityTest {

	private TileEntity tileEntity;
	
	@BeforeEach
    void setUp() {
        tileEntity = new TileEntity(false, true); // Initialize with default values
    }
	
	@Test
    void testConstructor() {
        assertNotNull(tileEntity);
        assertFalse(tileEntity.isColliderOn(), "Collider should be off by default");
        assertTrue(tileEntity.isNeedPathBkgrd(), "Background path should be needed");
    }
	
	@Test
    void testSetColliderOnOff() {
        tileEntity.setColliderOnOff(true);
        assertTrue(tileEntity.isColliderOn(), "Collider should be on after setting to true");
        
        tileEntity.setColliderOnOff(false);
        assertFalse(tileEntity.isColliderOn(), "Collider should be off after setting to false");
    }

    @Test
    void testIsNeedPathBkgrd() {
        assertTrue(tileEntity.isNeedPathBkgrd(), "Background path should be needed by default");

        TileEntity anotherTileEntity = new TileEntity(true, false);
        assertFalse(anotherTileEntity.isNeedPathBkgrd(), "Background path should not be needed when initialized with false");
    }

}
