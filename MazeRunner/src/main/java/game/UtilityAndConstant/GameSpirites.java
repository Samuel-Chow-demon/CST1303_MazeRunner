package game.UtilityAndConstant;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR;
import game.UtilityAndConstant.ConstantAndDefine.eTILE;

import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.*;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_SIZE_XY;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_TILE_SIZE;

public class GameSpirites {
	
	static Map<ePLAYER, Map<ePLAYER_SPRITE_DIR, BufferedImage>> playerImageMap = null;
	static Map<eTILE, BufferedImage> tileImageMap = null;
	
	private Object lockCreatePlayerImage = new Object();
	private Object lockCreateTileImage = new Object();
	
	public GameSpirites() 
	{}
	
	// GameSpirites Instance singleton
	private static class singletonHelper
	{
		private static final GameSpirites instance = new GameSpirites();
	}
	
	public static GameSpirites getInstance()
	{
		if (playerImageMap == null)
		{
			playerImageMap = new HashMap<>();
		}
		if (tileImageMap == null)
		{
			tileImageMap = new HashMap<>();
		}
		
		return singletonHelper.instance;
	}
	// GameSpirites Instance singleton

	public static Color PlayerColor(ePLAYER player)
	{
		switch (player)
		{
		default:
		case eP1:
			return Color.BLUE;
		case eP2:
			return Color.green;
		case eP3:
			return Color.MAGENTA;
		case eP4:
			return Color.ORANGE;
		}
	}
	
	public Map<ePLAYER_SPRITE_DIR, BufferedImage> getPlayerImage(ePLAYER player)
	{
		// Create if not yet load the image
		if (!playerImageMap.containsKey(player))
		{
			synchronized(this.lockCreatePlayerImage)
			{
				if (!playerImageMap.containsKey(player)) // check again for data race
				{ 
					playerImageMap.put(player, getPlayerBufferedImage(player));
				}
			}
		}
		return playerImageMap.get(player);
	}
	
	private BufferedImage getScaledImage(BufferedImage origImg, int XSize, int YSize)
	{
		BufferedImage scaledImg = new BufferedImage(XSize, YSize, origImg.getType());
		Graphics2D g2D = scaledImg.createGraphics();
		g2D.drawImage(origImg, 0, 0, XSize, YSize, null);
		return scaledImg;
	}
	
	private Map<ePLAYER_SPRITE_DIR, BufferedImage> getPlayerBufferedImage(ePLAYER player)
	{
		Map<ePLAYER_SPRITE_DIR, BufferedImage> map = new HashMap<>();
		
		final String up1Image = "_Up1.png";
		final String up2Image = "_Up2.png";
		final String dn1Image = "_Dn1.png";
		final String dn2Image = "_Dn2.png";
		final String lf1Image = "_Lf1.png";
		final String lf2Image = "_Lf2.png";
		final String rt1Image = "_Rt1.png";
		final String rt2Image = "_Rt2.png";
		
		String playerCharacter = ""; // default not valid
		
		switch(player)
		{
			default:
			case eP1:
				playerCharacter = "Kuma";
				break;
				
			case eP2:
				playerCharacter = "Slime";
				break;
			case eP3:
				playerCharacter = "Tokage";
				break;
			case eP4:
				playerCharacter = "Parrot";
				break;
		}
		
		StringBuilder packageRes = new StringBuilder("/sprite/player/");
		StringBuilder character = new StringBuilder(playerCharacter);
		StringBuilder path = new StringBuilder();
		
		try {
			// "/sprite.player/Kuma_Up1.png"
			String up1Res = path.append(packageRes).append(character).append(up1Image).toString();
			path.setLength(0);
			String up2Res = path.append(packageRes).append(character).append(up2Image).toString();
			path.setLength(0);
			String dn1Res = path.append(packageRes).append(character).append(dn1Image).toString();
			path.setLength(0);
			String dn2Res = path.append(packageRes).append(character).append(dn2Image).toString();
			path.setLength(0);
			String lf1Res = path.append(packageRes).append(character).append(lf1Image).toString();
			path.setLength(0);
			String lf2Res = path.append(packageRes).append(character).append(lf2Image).toString();
			path.setLength(0);
			String rt1Res = path.append(packageRes).append(character).append(rt1Image).toString();
			path.setLength(0);
			String rt2Res = path.append(packageRes).append(character).append(rt2Image).toString();
			path.setLength(0);
			
			// Check if the resource exists
	        InputStream resU1 = getClass().getResourceAsStream(up1Res);
	        InputStream resU2 = getClass().getResourceAsStream(up2Res);
	        InputStream resD1 = getClass().getResourceAsStream(dn1Res);
	        InputStream resD2 = getClass().getResourceAsStream(dn2Res);
	        InputStream resL1 = getClass().getResourceAsStream(lf1Res);
	        InputStream resL2 = getClass().getResourceAsStream(lf2Res);
	        InputStream resR1 = getClass().getResourceAsStream(rt1Res);
	        InputStream resR2 = getClass().getResourceAsStream(rt2Res);
	        
	        
	        // Only all the resources exist to load into ImageIO then to the map output
	        // Also create the scaled image directly for fast rendering after
	        if (resU1 != null && resU2 != null && resD1 != null && resD2 != null &&
	        	resL1 != null && resL2 != null && resR1 != null && resR2 != null)
	        {  
				map.put(eUP_1, getScaledImage(ImageIO.read(resU1), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
				map.put(eUP_2, getScaledImage(ImageIO.read(resU2), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
				map.put(eDOWN_1, getScaledImage(ImageIO.read(resD1), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
				map.put(eDOWN_2, getScaledImage(ImageIO.read(resD2), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
				map.put(eLEFT_1, getScaledImage(ImageIO.read(resL1), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
				map.put(eLEFT_2, getScaledImage(ImageIO.read(resL2), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
				map.put(eRIGHT_1, getScaledImage(ImageIO.read(resR1), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
				map.put(eRIGHT_2, getScaledImage(ImageIO.read(resR2), PLAYER_SIZE_XY, PLAYER_SIZE_XY));
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.clear();
		}
		
		return map;
	}
	
	private BufferedImage getTileEntityBufferedImage(eTILE type)
	{
		String entity = ""; // default not valid
		
		switch(type)
		{
			case ePATH:
				entity = "Path1";
				break;
			case eWALL:
				entity = "Wall1";
				break;
			case eTREE:
				entity = "Tree1";
				break;
			case eFLAG:
				entity = "Flag1";
			default:
				break;
		}
		
		if (entity != "")
		{
			StringBuilder packageRes = new StringBuilder("/sprite/entity/");
			StringBuilder entityDir = new StringBuilder(entity);
			StringBuilder path = new StringBuilder();
			
			InputStream res = getClass().getResourceAsStream(path.append(packageRes).append(entityDir).append(".png").toString());
			if (res != null)
			{
				try {
					return getScaledImage(ImageIO.read(res), GAME_TILE_SIZE, GAME_TILE_SIZE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public BufferedImage getMazeTileImage(eTILE type)
	{
		// Create if not yet load the image
		if (!tileImageMap.containsKey(type))
		{
			synchronized(this.lockCreateTileImage)
			{
				if (!tileImageMap.containsKey(type)) // check again for data race
				{ 
					tileImageMap.put(type, getTileEntityBufferedImage(type));
				}
			}
		}
				
		return tileImageMap.get(type);
	}
	
	public static Color TileColor(eTILE type)
	{
		switch (type)
		{
			default:
			case ePATH:
				return Color.GRAY;
			case eWALL:
				return Color.decode("#825118");
		}
	}
}
