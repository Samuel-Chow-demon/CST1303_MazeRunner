package game.Maze;

import static game.UtilityAndConstant.ConstantAndDefine.GAME_BOARD_COLUMN;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_BOARD_ROW;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_TILE_SIZE;
import static game.UtilityAndConstant.ConstantAndDefine.eTILE.ePATH;
import static game.UtilityAndConstant.ConstantAndDefine.eTILE.eTREE;
import static game.UtilityAndConstant.ConstantAndDefine.eTILE.eWALL;
import static game.UtilityAndConstant.ConstantAndDefine.eTILE.eFLAG;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import game.Player.Player;
import game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;
import game.UtilityAndConstant.GameSpirites;

public class PlayerMazeManager 
{
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private eTILE[][] curMazeTile;							// Storing the Maze map with eTile type number with col row position
	private Map<eTILE, TileEntity> mapTileTypeAttrib;		// Storing each tile type having attributes
	
	public PlayerMazeManager()
	{
		curMazeTile = null;
		SetTileTypeAttribute();
	}
	
	private void SetTileTypeAttribute()
	{
		mapTileTypeAttrib = new HashMap<>();
		mapTileTypeAttrib.put(ePATH, new TileEntity(false, false));		// Pathway to move
		mapTileTypeAttrib.put(eWALL, new TileEntity(true, false));		// Wall to block
		mapTileTypeAttrib.put(eTREE, new TileEntity(true, true));		// Tree to block
		mapTileTypeAttrib.put(eFLAG, new TileEntity(false, true));		// Flag to allow move over
	}
	
	public void setMazeTileMap(eTILE[][] mapTile)
	{
		lock.writeLock().lock();
        try {
        	this.curMazeTile = mapTile;
        } finally {
            lock.writeLock().unlock();
        }
	}
	
	public void draw(Graphics2D g)
	{
		if (curMazeTile != null)
		{
			lock.readLock().lock();
			
			int x = 0;		// The global pixel coordinate for graphic draw, x,y is top left corner where x --> +ve  ,  y (downward) +ve
			int y = 0;
			int col = 0;	// The Column Row for Each Tile unit (One Tile had 48 x 48 pixel)
			int row = 0;
			
			while (row < curMazeTile.length && row < GAME_BOARD_ROW &&
					col < curMazeTile[row].length && col < GAME_BOARD_COLUMN)
			{
				draw(g, x, y, curMazeTile[row][col]);
				col++;
				x += GAME_TILE_SIZE;
				
				if (col == curMazeTile[row].length || col == GAME_BOARD_COLUMN)
				{
					// Reset for column
					col = 0;
					x = 0;
					
					// Then goto next row
					row++;
					y += GAME_TILE_SIZE;
				}
			}
			lock.readLock().unlock();
		}
	}
	
	private void draw(Graphics2D g, int x, int y, eTILE eTileType)
	{	
		BufferedImage image = GameSpirites.getInstance().getMazeTileImage(eTileType);
		if (image != null)
		{
			// If the Map Entity need to draw the path background first
			if (mapTileTypeAttrib.containsKey(eTileType) &&
				mapTileTypeAttrib.get(eTileType).isNeedPathBkgrd())
			{
				BufferedImage imagePath = GameSpirites.getInstance().getMazeTileImage(ePATH);
				g.drawImage(imagePath, x, y, GAME_TILE_SIZE, GAME_TILE_SIZE, null);
			}
			
			g.drawImage(image, x, y, GAME_TILE_SIZE, GAME_TILE_SIZE, null);
		}
		// if do not have the resources image, return a colored rect
		else
		{
			// Set Color
			g.setColor(GameSpirites.TileColor(eTileType));
			
			// Position XY, Size XY
			g.fillRect(x, y, GAME_TILE_SIZE, GAME_TILE_SIZE);
		}
	}
	
	private Boolean isCollided(eTILE eType1, eTILE eType2)
	{
		return (mapTileTypeAttrib.containsKey(eType1) && mapTileTypeAttrib.get(eType1).isColliderOn()) ||
				(mapTileTypeAttrib.containsKey(eType2) && mapTileTypeAttrib.get(eType2).isColliderOn());
	}
	
	public void CollisionCheckAndMove(Player player, eCONTROL_KEY eDir)
	{
		Rectangle playerCollider = player.getCollider();
		
		int playerColliderPosLeftX 	 = player.getX() + playerCollider.x;
		int playerColliderPosRightX  = playerColliderPosLeftX + playerCollider.width;
		int playerColliderPosTopY    = player.getY() + playerCollider.y;
		int playerColliderPosBottomY = playerColliderPosTopY + playerCollider.height;
		
		int checkTileMapLeftColIdx   = playerColliderPosLeftX / GAME_TILE_SIZE;
		int checkTileMapRightColIdx  = playerColliderPosRightX / GAME_TILE_SIZE;
		
		int checkTileMapTopRowIdx    = playerColliderPosTopY / GAME_TILE_SIZE;
		int checkTileMapBottomRowIdx = playerColliderPosBottomY / GAME_TILE_SIZE;
		
		// Set direction first
		player.setDirection(eDir);
		
		// At most would hit two game map tile at a time
		eTILE checkTile1Type, checkTile2Type;
		
		switch (eDir)
		{
			case eUP:
				checkTileMapTopRowIdx = (playerColliderPosTopY - player.getSpeed()) / GAME_TILE_SIZE; // consider to foresee move up with speed
				checkTile1Type = curMazeTile[checkTileMapTopRowIdx][checkTileMapLeftColIdx];
				checkTile2Type = curMazeTile[checkTileMapTopRowIdx][checkTileMapRightColIdx];
				
				if (!isCollided(checkTile1Type, checkTile2Type)) // if not collided can allow to move up
				{
					player.moveUp();
				}
				break;
				
			case eDOWN:
				checkTileMapBottomRowIdx = (playerColliderPosBottomY + player.getSpeed()) / GAME_TILE_SIZE; // consider to foresee move down with speed
				checkTile1Type = curMazeTile[checkTileMapBottomRowIdx][checkTileMapLeftColIdx];
				checkTile2Type = curMazeTile[checkTileMapBottomRowIdx][checkTileMapRightColIdx];
				
				if (!isCollided(checkTile1Type, checkTile2Type)) // if not collided can allow to move down
				{
					player.moveDown();
				}
				break;
				
			case eLEFT:
				checkTileMapLeftColIdx = (playerColliderPosLeftX - player.getSpeed()) / GAME_TILE_SIZE; // consider to foresee move left with speed
				checkTile1Type = curMazeTile[checkTileMapTopRowIdx][checkTileMapLeftColIdx];
				checkTile2Type = curMazeTile[checkTileMapBottomRowIdx][checkTileMapLeftColIdx];
				
				if (!isCollided(checkTile1Type, checkTile2Type)) // if not collided can allow to move left
				{
					player.moveLeft();
				}
				
				break;
				
			case eRIGHT:
				checkTileMapRightColIdx = (playerColliderPosRightX + player.getSpeed()) / GAME_TILE_SIZE; // consider to foresee move right with speed
				checkTile1Type = curMazeTile[checkTileMapTopRowIdx][checkTileMapRightColIdx];
				checkTile2Type = curMazeTile[checkTileMapBottomRowIdx][checkTileMapRightColIdx];
				
				if (!isCollided(checkTile1Type, checkTile2Type)) // if not collided can allow to move Right
				{
					player.moveRight();
				}
				break;
				
			default:
				break;
		}
	}
	
	public void CheckIfPlayerWin(Player player)
	{
		Point finishPt = player.getFinishPos();
		Rectangle finishArea = new Rectangle(finishPt.x + (GAME_TILE_SIZE / 2), finishPt.y + (GAME_TILE_SIZE / 2), GAME_TILE_SIZE, GAME_TILE_SIZE);
		
		Rectangle playerCollider = player.getCollider();
		
		// if the finish area intersect player current collider position
		if (finishArea.intersects(player.getX() + playerCollider.x, player.getY() + playerCollider.y, playerCollider.width, playerCollider.height))
		{
			player.setWin();
		}
	}
}
