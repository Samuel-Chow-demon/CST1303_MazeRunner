package game.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;

import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.*;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

public class InputKeyHandler implements KeyListener {

	// Up, Down, Left, Right
	private Boolean[] isKeyPressed = new Boolean[eTOTAL_KEY.ordinal()];
	
	// KeyCode, Direction eNum
	private Map<Integer, Integer> keyMap;
	
	public InputKeyHandler(ePLAYER id)
	{
		// init all flags to false
		Arrays.fill(isKeyPressed, false);
		
		keyMap = new HashMap();
		switch (id)
		{
		case eP4:
			keyMap.put(KeyEvent.VK_NUMPAD8, eUP.ordinal());
			keyMap.put(KeyEvent.VK_NUMPAD5, eDOWN.ordinal());
			keyMap.put(KeyEvent.VK_NUMPAD4, eLEFT.ordinal());
			keyMap.put(KeyEvent.VK_NUMPAD6, eRIGHT.ordinal());
			keyMap.put(KeyEvent.VK_ENTER, eENTER.ordinal());
			break;
		case eP3:
			keyMap.put(KeyEvent.VK_I, eUP.ordinal());
			keyMap.put(KeyEvent.VK_K, eDOWN.ordinal());
			keyMap.put(KeyEvent.VK_J, eLEFT.ordinal());
			keyMap.put(KeyEvent.VK_L, eRIGHT.ordinal());
			keyMap.put(KeyEvent.VK_ENTER, eENTER.ordinal());
			break;
		case eP2:
			keyMap.put(KeyEvent.VK_W, eUP.ordinal());
			keyMap.put(KeyEvent.VK_S, eDOWN.ordinal());
			keyMap.put(KeyEvent.VK_A, eLEFT.ordinal());
			keyMap.put(KeyEvent.VK_D, eRIGHT.ordinal());
			keyMap.put(KeyEvent.VK_ENTER, eENTER.ordinal());
			break;
		
		default:
		case eP1:
			keyMap.put(KeyEvent.VK_UP, eUP.ordinal());
			keyMap.put(KeyEvent.VK_DOWN, eDOWN.ordinal());
			keyMap.put(KeyEvent.VK_LEFT, eLEFT.ordinal());
			keyMap.put(KeyEvent.VK_RIGHT, eRIGHT.ordinal());
			keyMap.put(KeyEvent.VK_ENTER, eENTER.ordinal());
			break;
		}
	}
	
	public String getPlayerControlDescription(ePLAYER id)
	{
		switch (id)
		{
		case eP4:
			return "(NumPad : UP-8  DOWN-5  LEFT-4  RIGHT-6)";
		case eP3:
			return "(UP-I  DOWN-K  LEFT-J  RIGHT-L)";
		case eP2:
			return "(UP-W  DOWN-S  LEFT-A  RIGHT-D)";
		
		default:
		case eP1:
			return "(Arrow : UP  DOWN  LEFT  RIGHT)";
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		
		if (keyMap.containsKey(keyCode))
		{
			isKeyPressed[keyMap.get(keyCode)] = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		
		if (keyMap.containsKey(keyCode))
		{
			isKeyPressed[keyMap.get(keyCode)] = false;
		}
	}
	
	public Boolean isKeyPressed(eCONTROL_KEY ekey)
	{
		return isKeyPressed[ekey.ordinal()];
	}
	

}
