package game.Player;

import static game.UtilityAndConstant.ConstantAndDefine.COMMAND_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_KEY_CLOSE;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_KEY_ID;
import static game.UtilityAndConstant.ConstantAndDefine.ENTITY_KEY_REQUEST;
import static game.UtilityAndConstant.ConstantAndDefine.FPS;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_END_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.GAME_TILE_SIZE;
import static game.UtilityAndConstant.ConstantAndDefine.MAP_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_DATA_KEY;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_SIZE_XY;
import static game.UtilityAndConstant.ConstantAndDefine.SPRITE_PLAY_FPS;
import static game.UtilityAndConstant.ConstantAndDefine.UI_HEIGHT;
import static game.UtilityAndConstant.ConstantAndDefine.UI_WIDTH;
import static game.UtilityAndConstant.ConstantAndDefine.iENSURE_EACH_LOOP_IN_NS;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eDOWN;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eENTER;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eLEFT;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eRIGHT;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eUP;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.eMAX_PLAYER;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eDOWN_1;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eDOWN_2;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eLEFT_1;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eLEFT_2;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eRIGHT_1;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eRIGHT_2;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eTOTAL_SPRITE_DIR;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eUP_1;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR.eUP_2;
import static game.UtilityAndConstant.JPanelUtility.eALIGNMENT.eALIGN_CENTER;

import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.eP1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import game.Maze.MazeData;
import game.Maze.PlayerMazeManager;
import game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER_SPRITE_DIR;
import game.UtilityAndConstant.DataFormatCenter;
import game.UtilityAndConstant.GameSpirites;
import game.UtilityAndConstant.JPanelUtility;
import game.UtilityAndConstant.NoArgFunction;
import game.UtilityAndConstant.ThreadHandler;
import game.UtilityAndConstant.UniFunction;

public class MazeGame extends JPanel implements Runnable, AutoCloseable{
	
	private ExecutorService playerUIExecutor;
	private Boolean isShutDown;
	private InputKeyHandler keyInput;
	private Player ownPlayer;
	private UniFunction<ByteBuffer, Void> funcCallBackSendMessage;
	private NoArgFunction<Void> funcCallBackAfterChkCreatePlayer;
	private NoArgFunction<Void> funcCallBackCloseSession;
	private NoArgFunction<Void> funcCallBackQuitGame;
	
	private NoArgFunction<Void> funcCallBackInitWaitReceiveMapData;
	private NoArgFunction<Void> funcCallBackFinishReceiveMapData;
	
	private Map<ePLAYER, Player> mapPlayerRender;
	private Boolean bFirstCreateUpdate;
	private String createPlayerErrorMsg;
	
	private Integer[] arrSpriteIdx;
	private Boolean[] arrIsPlayerMove;
	private Integer spriteCountFPSPlay;
	
	private PlayerMazeManager mazeManager;
	
	private Boolean isComPlayer;
	private Long nextComUpdateTime, nextComDirUpdateTime;
	private eCONTROL_KEY eLastComDir;
		
	private enum eGAME_STATE{
		eGAME, eEND, eTOTAL_STATE;
	}
	private eGAME_STATE eState;
	
	private Player winGamePlayer;
	private ePLAYER_SPRITE_DIR eWinnerSpirteIdx;
	
	private enum eEND_GAME_COMMAND{
		eRESTART, eQUIT, eTOTAL_END_CMD;
		
		static public eEND_GAME_COMMAND fromValue(int val)
		{
			switch(val)
			{
				case 0:
					return eRESTART;
				case 1:
					return eQUIT;
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	private eEND_GAME_COMMAND eEndGameCmd;
	 
	public MazeGame(Boolean isComPlayer)
	{
		this.setPreferredSize(new Dimension(UI_WIDTH, UI_HEIGHT));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); 			// Important can improve rendering performace for JPanel
		this.setFocusable(true);      			// set to accept key input trigger
		
		playerUIExecutor = null;
		isShutDown = false;
		
		funcCallBackSendMessage = null;
		funcCallBackAfterChkCreatePlayer = null;
		funcCallBackInitWaitReceiveMapData = null;
		funcCallBackFinishReceiveMapData = null;
		funcCallBackQuitGame = null;
		
		bFirstCreateUpdate = false;				// init as false first
		createPlayerErrorMsg = "";				// default no error
		
		arrSpriteIdx = new Integer[eMAX_PLAYER.ordinal()];		
		Arrays.fill(arrSpriteIdx, 0);							// default idx 0
		
		arrIsPlayerMove = new Boolean[eMAX_PLAYER.ordinal()];
		Arrays.fill(arrIsPlayerMove, false);
		
		spriteCountFPSPlay = 0;
		
		// Here would load the all the available map file
		mazeManager = new PlayerMazeManager();
		
		winGamePlayer = null;
		eWinnerSpirteIdx = eUP_1;
		eEndGameCmd = eEND_GAME_COMMAND.eRESTART;
		
		this.isComPlayer = isComPlayer;
		nextComUpdateTime = (long)0;
		nextComDirUpdateTime = (long)0;
		eLastComDir = eDOWN;
		
		SetGameState(eGAME_STATE.eGAME);
	}
	
	private void RestartGame()
	{
		ownPlayer.resetAttributes();
		winGamePlayer = null;
		eWinnerSpirteIdx = eUP_1;
		eEndGameCmd = eEND_GAME_COMMAND.eRESTART;
		
		bFirstCreateUpdate = true;
		SetGameState(eGAME_STATE.eGAME);
	}
	
	private void SetGameState(eGAME_STATE eState)
	{
		this.eState = eState;
	}
	private eGAME_STATE GetGameState()
	{
		return this.eState;
	}
	
	public void SetCallBackSendMessage(UniFunction<ByteBuffer, Void> func)
	{
		this.funcCallBackSendMessage = func;
	}
	public void SetCallBackSuccessCreatePlayer(NoArgFunction<Void> func)
	{
		this.funcCallBackAfterChkCreatePlayer = func;
	}
	public void SetCallBackCloseSession(NoArgFunction<Void> func)
	{
		this.funcCallBackCloseSession = func;
	}
	public void SetCallBackQuitGame(NoArgFunction<Void> func)
	{
		this.funcCallBackQuitGame = func;
	}
	public String getCreatePlayerErrMessage()
	{
		return createPlayerErrorMsg;
	}
	public void SetCallBackInitWaitMazeData(NoArgFunction<Void> func)
	{
		this.funcCallBackInitWaitReceiveMapData = func;
	}
	public void SetCallBackFinishReceiveMazeData(NoArgFunction<Void> func)
	{
		this.funcCallBackFinishReceiveMapData = func;
	}
	
	private void CreatePlayer(ePLAYER id)
	{
		keyInput = new InputKeyHandler(id);
		this.addKeyListener(keyInput);			// Pass in the key input object
		
		ownPlayer = new Player(id);
		mapPlayerRender = new HashMap<>();
		mapPlayerRender.put(id, ownPlayer);
		bFirstCreateUpdate = true;				// when player created, set flag to send data to server for record and broadcast first position
		createPlayerErrorMsg = "";				// Success no Error
		
		// Set need wait Receive Maze Data before trigger the create player success
		if (funcCallBackInitWaitReceiveMapData != null)
		{
			funcCallBackInitWaitReceiveMapData.apply();
		}
		
		if (funcCallBackAfterChkCreatePlayer != null)
		{
			funcCallBackAfterChkCreatePlayer.apply();
		}
		
		// at the end of create player, start request Maze data
		RequestMazeData();
	}
	
	private void RequestMazeData()
	{	
		Map<String, String> mapRequest = Map.of(ENTITY_KEY_REQUEST, "");
		SendMessage(DataFormatCenter.createKeyByteBufferData(MAP_DATA_KEY, mapRequest));
	}
	
	private void SetReturnMaxPlayerReachFail()
	{
		createPlayerErrorMsg = "Max Player Reached.";
		
		if (funcCallBackAfterChkCreatePlayer != null)
		{
			funcCallBackAfterChkCreatePlayer.apply();
		}
	}
	
	public void StartGame()
	{
		if (playerUIExecutor == null)
		{
			playerUIExecutor = Executors.newSingleThreadExecutor();
			playerUIExecutor.submit(this);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		double eachFrameInterval = 1_000_000_000 / FPS; // 1 second in nano / 60 frame
		double loopFrameIntervalPortion = 0;
		long currentLoopStartTime;
		long lastLoopStartTime = System.nanoTime();
		//long timeThatRun = 0;
		//int frameDrawnCount = 0;
		
		while(!isShutDown)
		{
			currentLoopStartTime = System.nanoTime();
			
			double lastLoopUsedTime = (currentLoopStartTime - lastLoopStartTime);
			
			loopFrameIntervalPortion += (lastLoopUsedTime / eachFrameInterval);
			
			//timeThatRun += lastLoopUsedTime;
			
			lastLoopStartTime = currentLoopStartTime;
			
			// Use double timing calculate action filtering is more accurate than using ThreadSleep()
			if (loopFrameIntervalPortion >= 1) // the accummulated portion reached or more than 1 Frame timing
			{
				// Update game widget
				update();
			
				// Rendering Screen
				repaint(); // call JPanel to run paintComponent
				
				// Check if need to update the sprite idx
				updateSpriteIdx();
				
				loopFrameIntervalPortion--; // minus 1 frame size portion
				
				//frameDrawnCount++;
			}
			
//			if (timeThatRun >= 1_000_000_000) // per 1 sec
//			{
//				System.out.println("FPS : " + frameDrawnCount);
//				frameDrawnCount = 0;
//				timeThatRun -= 1_000_000_000;
//			}
			
			CheckAndWaitForEachLoop(currentLoopStartTime);
		}
	}
	
	private void CheckAndWaitForEachLoop(long loopStartTime)
	{
		long remainTimeNeedWaitInNS = iENSURE_EACH_LOOP_IN_NS - (System.nanoTime() - loopStartTime);
        if (remainTimeNeedWaitInNS > 0) 
        {
            // loop took less than 1ms, sleep for the remaining time
            try {
				Thread.sleep(remainTimeNeedWaitInNS / iENSURE_EACH_LOOP_IN_NS, (int)(remainTimeNeedWaitInNS % iENSURE_EACH_LOOP_IN_NS));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	private void RunGameState()
	{
		boolean bUpdated = false;
		
		if (isComPlayer)
		{
			Boolean needUpdate = (System.nanoTime() - nextComUpdateTime > 0) ? true : false;
			if (nextComUpdateTime == 0 || needUpdate)
			{
				Random rand = new Random();
				
				Boolean isCollided = mazeManager.CollisionCheckAndMove(ownPlayer, eLastComDir);
				
				if (isCollided || (System.nanoTime() - nextComDirUpdateTime > 0))
				{
					int randomDir = rand.nextInt(4);
					eCONTROL_KEY eKeyDir = eCONTROL_KEY.fromValue(randomDir);
					while (eKeyDir == eLastComDir)
					{
						randomDir = rand.nextInt(4);
						eKeyDir = eCONTROL_KEY.fromValue(randomDir);
					}
					eLastComDir = eKeyDir;
					
					nextComDirUpdateTime = System.nanoTime() + ((rand.nextInt(700) * 1_000_000) + 300_000_000); // next Dir update would be 300 - 1000 milli second after
				}
				
				nextComUpdateTime = System.nanoTime() + ((rand.nextInt(50) * 1_000_000)); // next update would be 0 - 50 milli second after
				
				bUpdated = true;
			}
		}
		else
		{	
			// Check Player Control
			if (keyInput.isKeyPressed(eUP))
			{
				mazeManager.CollisionCheckAndMove(ownPlayer, eUP);
				bUpdated = true;
			}
			else if (keyInput.isKeyPressed(eDOWN))
			{
				mazeManager.CollisionCheckAndMove(ownPlayer, eDOWN);
				bUpdated = true;
			}
			
			if (keyInput.isKeyPressed(eLEFT))
			{
				mazeManager.CollisionCheckAndMove(ownPlayer, eLEFT);
				bUpdated = true;
			}
			else if (keyInput.isKeyPressed(eRIGHT))
			{
				mazeManager.CollisionCheckAndMove(ownPlayer, eRIGHT);
				bUpdated = true;
			}
		}
		
		if (bUpdated || bFirstCreateUpdate)
		{
			bFirstCreateUpdate = false;
			
			mazeManager.CheckIfPlayerWin(ownPlayer);
			
			// Immediate send first
			// data format - "PLY Player1 id x y speed 0 0" // start with PLY - Player data key [last two is "IsWin,IsDie"]
			SendMessage(DataFormatCenter.createPlayerByteBufferData(ownPlayer));
			
			// then modify the map for render
			mapPlayerRender.put(ownPlayer.getID(), ownPlayer);
			
			arrIsPlayerMove[ownPlayer.getID().ordinal()] = true;
		}
		else
		{
			arrIsPlayerMove[ownPlayer.getID().ordinal()] = false;
		}
	}
	
	private void RunEndState()
	{
		// Check Player Control
		if (keyInput.isKeyPressed(eUP))
		{
			if (eEndGameCmd.ordinal() > 0)
			{
				int idx = eEndGameCmd.ordinal();
				eEndGameCmd = eEND_GAME_COMMAND.fromValue(--idx);
			}
		}
		else if (keyInput.isKeyPressed(eDOWN))
		{
			if (eEndGameCmd.ordinal() < eEND_GAME_COMMAND.eTOTAL_END_CMD.ordinal() - 1)
			{
				int idx = eEndGameCmd.ordinal();
				eEndGameCmd = eEND_GAME_COMMAND.fromValue(++idx);
			}
		}
		else if (keyInput.isKeyPressed(eENTER))
		{
			switch (eEndGameCmd)
			{
			case eRESTART:
				RestartGame();
				break;
			case eQUIT:
				if (this.funcCallBackQuitGame != null)
				{
					this.funcCallBackQuitGame.apply();
				}
				break;
			}
		}
		
		
	}
	
	
	private void update()
	{		
		switch(GetGameState())
		{
			case eGAME:
				RunGameState();
				break;
				
			case eEND:
				RunEndState();
				break;
				
			default:
				break;
		}
	}
	
	private void updateSpriteIdx()
	{
		// Since each upate is 60 FPS, thus for sprite to play with 5 FPS
		// it counts up to the 60 / 5 on doing the sprite play switch
		spriteCountFPSPlay++;
		if (spriteCountFPSPlay >= (FPS / SPRITE_PLAY_FPS))
		{
			for (int i = 0; i < eMAX_PLAYER.ordinal(); i++)
			{
				if (arrIsPlayerMove[i] == true) // only that player move to toggle the sprite
				{
					// Use XOR can toggle between 0 and 1
					arrSpriteIdx[i] ^= 1;
					
					arrIsPlayerMove[i] = false; // then reset back the move flag
				}
			}
			
			if (GetGameState() == eGAME_STATE.eEND)
			{
				updateWinnerSpriteIdx();
			}
			
			// Reset
			spriteCountFPSPlay = 0;
		}
	}
	
	private void updateWinnerSpriteIdx()
	{
		int curIdx = eWinnerSpirteIdx.ordinal();
		if (curIdx < eTOTAL_SPRITE_DIR.ordinal() - 1)
		{
			eWinnerSpirteIdx = ePLAYER_SPRITE_DIR.fromValue(++curIdx);
		}
		else
		{
			eWinnerSpirteIdx = eUP_1;
		}
	}
	
	private void SendMessage(ByteBuffer data)
	{
		if (this.funcCallBackSendMessage != null)
		{
			this.funcCallBackSendMessage.apply(data);
		}
	}
	
	// Would be callbacked to run by GameClient when message receive
	public void onMessageReceived(ByteBuffer data)
	{
		// the return only had one set of map data [DataType, DataInputStream]
		Map<String, ByteBuffer> mapDataType = DataFormatCenter.getDataStream(data);
		
		for (Map.Entry<String, ByteBuffer> entry : mapDataType.entrySet())
		{
			switch (entry.getKey())
			{
				case COMMAND_DATA_KEY:	// CMD
				{
					Map<String, String> mapEntityVal = DataFormatCenter.getEntityData(entry.getValue()); // pass the buffer
					
					for (Map.Entry<String, String> entryEntity : mapEntityVal.entrySet())
					{
						String key = entryEntity.getKey();
						String val = entryEntity.getValue();
						
						switch (key)
						{
							case ENTITY_KEY_ID: // "ID", Server send to player to assign the Player ID
								CreatePlayer(ePLAYER.fromValue(val));
								break;
							
							// Other command add here
							case ENTITY_KEY_CLOSE: // "CLOSE", Server need player to close session
								SetReturnMaxPlayerReachFail();
								break;
								
							default:
								break;	
						}
					}
			
					break;
				}
				
				case GAME_END_KEY: // END
				{
					Map<String, String> mapEntityVal = DataFormatCenter.getEntityData(entry.getValue()); // pass the buffer
					
					for (Map.Entry<String, String> entryEntity : mapEntityVal.entrySet())
					{
						String key = entryEntity.getKey();
						String val = entryEntity.getValue();
						
						switch (key)
						{
							case ENTITY_KEY_ID: // "ID", Server send to player which player win the game
								winGamePlayer = new Player(mapPlayerRender.get(ePLAYER.fromValue(val))); // deep copy the winning player object
								SetGameState(eGAME_STATE.eEND);
								break;
							
							default:
								break;	
						}
					}
					break;
				}
				
				case MAP_DATA_KEY: // MAP
				{
					MazeData mazeData = DataFormatCenter.getMazeMapData(entry.getValue());
					
					mazeManager.setMazeTileMap(mazeData.getMapTile());
					ownPlayer.setStartPos(mazeData.getStartPt());
					ownPlayer.setFinishPos(mazeData.getEndPt());
					
					funcCallBackFinishReceiveMapData.apply();
					break;
				}
					
				default:
				case PLAYER_DATA_KEY:	// PLY
				{
					Player player = DataFormatCenter.getOtherPlayerData(entry.getValue()); // pass the buffer
					
					switch (GetGameState())
					{
						case eGAME_STATE.eGAME:
						{
							ePLAYER playerID = player.getID();
							
							arrIsPlayerMove[playerID.ordinal()] = true;
							
							mapPlayerRender.put(playerID, player);
							break;
						}
						case eGAME_STATE.eEND:
						{
							if (player.getIsDie() == 1)
							{
								ePLAYER playerID = player.getID();
								mapPlayerRender.put(playerID, player);
							}
							break;
						}
						
					}
					break;
				}
			}
		}
	}
	
	// Override JPanel paintComponent
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		
		// Draw the background first
		mazeManager.draw(g2D);
		
		for(Player player : mapPlayerRender.values())
		{
			if (player.getIsDie() == 0) // alive to draw
			{
				drawPlayer(player, g2D);
			}
		}
		
		if (GetGameState() == eGAME_STATE.eEND)
		{
			drawEndGameMenuSubWindow(g2D, winGamePlayer);
		}
		
		g2D.dispose(); // release the Graphic resources after drawn
	}
	
	private void drawPlayer(Player player, Graphics2D g)
	{
		BufferedImage image = null;
		
		ePLAYER playerID = player.getID();
		
		Map<ePLAYER_SPRITE_DIR, BufferedImage> mapImage = GameSpirites.getInstance().getPlayerImage(playerID);
		
		ePLAYER_SPRITE_DIR dir = eDOWN_1;
		
		switch(player.getCurDir())
		{
			case eUP:
				dir = arrSpriteIdx[playerID.ordinal()] == 0 ? eUP_1 : eUP_2;
				//image = ImageIO.read(getClass().getResourceAsStream("/sprite/player/Kuma_Up1.png"));
				break;
			default:
			case eDOWN:
				dir = arrSpriteIdx[playerID.ordinal()] == 0 ? eDOWN_1 : eDOWN_2;
				//image = ImageIO.read(getClass().getResourceAsStream("/sprite/player/Kuma_Dn1.png"));
				break;
				
			case eLEFT:
				dir = arrSpriteIdx[playerID.ordinal()] == 0 ? eLEFT_1 : eLEFT_2;
				//image = ImageIO.read(getClass().getResourceAsStream("/sprite/player/Kuma_Lf1.png"));
				break;
				
			case eRIGHT:
				dir = arrSpriteIdx[playerID.ordinal()] == 0 ? eRIGHT_1 : eRIGHT_2;
				//image = ImageIO.read(getClass().getResourceAsStream("/sprite/player/Kuma_Rt1.png"));
				break;
		}
		
		if (mapImage.containsKey(dir))
		{
			image = GameSpirites.getInstance().getPlayerImage(playerID).get(dir);
		}
		
		if (image != null)
		{
			g.drawImage(image, player.getX(), player.getY(), PLAYER_SIZE_XY, PLAYER_SIZE_XY, null);
		}
		// if do not have the resources image, return a simple colored rect
		else
		{
			// Set Color / Spirtes after
			g.setColor(GameSpirites.PlayerColor(playerID));
			
			// Position XY, Size XY
			g.fillRect(player.getX(), player.getY(), PLAYER_SIZE_XY, PLAYER_SIZE_XY);
		}
	}
	
	private void drawEndGameMenuSubWindow(Graphics2D g, Player player)
	{
		if (player == null)
		{
			return;
		}
		
		// Draw Pointer
		Point drawPos = new Point(GAME_TILE_SIZE, GAME_TILE_SIZE);
		
		// Position
		int width = UI_WIDTH - (GAME_TILE_SIZE * 2);
		int height = (UI_HEIGHT * 2) / 3;
		
		// Setup Prompt Window
		Rectangle subWinRect = new Rectangle(drawPos.x, drawPos.y, width, height);
		drawPos = JPanelUtility.drawSubWindow(g, subWinRect, new Color(0, 0, 0, 0.8f),
												5, Color.WHITE, 5);
		
		// Display Player Win Message
		StringBuilder builder = new StringBuilder();
		builder.append(player.getName()).append(" WINS !!!");
		
		drawPos.y += GAME_TILE_SIZE * 3;
		drawPos = JPanelUtility.drawHorizontalAlignStringToParent(g, builder.toString(), Color.WHITE, 80F, subWinRect, eALIGN_CENTER, drawPos.y,
																	5, Color.GRAY);
		
		// Render Player Character
		int characterScale = 3;
		Map<ePLAYER_SPRITE_DIR, BufferedImage> mapImage = GameSpirites.getInstance().getPlayerImage(player.getID());
		int x = subWinRect.width / 2 - (GAME_TILE_SIZE * characterScale) / 2 + subWinRect.x;
		drawPos.y += GAME_TILE_SIZE * 2;
		
		BufferedImage image = GameSpirites.getInstance().getPlayerImage(player.getID()).get(eWinnerSpirteIdx);
		g.drawImage(image, x, drawPos.y, GAME_TILE_SIZE * characterScale, GAME_TILE_SIZE * characterScale, null); // reference to the Prompt window y
		
		// Menu
		// ReStart
		drawPos.y += GAME_TILE_SIZE * characterScale;
		drawPos.y += GAME_TILE_SIZE * 2;
		drawPos = JPanelUtility.drawHorizontalAlignStringToParent(g, "RESTART", Color.WHITE, 48F, subWinRect, eALIGN_CENTER, drawPos.y);
		if (eEndGameCmd == eEND_GAME_COMMAND.eRESTART)
		{
			g.drawString(">", drawPos.x - GAME_TILE_SIZE, drawPos.y);
		}
		
		// Quit
		drawPos.y += GAME_TILE_SIZE;
		drawPos = JPanelUtility.drawHorizontalAlignStringToParent(g, "QUIT", Color.WHITE, 48F, subWinRect, eALIGN_CENTER, drawPos.y);
		if (eEndGameCmd == eEND_GAME_COMMAND.eQUIT)
		{
			g.drawString(">", drawPos.x - GAME_TILE_SIZE, drawPos.y);
		}
	}
	
	public String getPlayerName()
	{
		if (ownPlayer != null)
		{
			return ownPlayer.getName();
		}
		return "";
	}
	public String getPlayerControlDescription()
	{
		if (ownPlayer != null)
		{
			return keyInput.getPlayerControlDescription(ownPlayer.getID());
		}
		return "";
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
		// Send out this player quit, set the player isDie = 1 then send to server
		if (ownPlayer != null)
		{
			ownPlayer.setDie();
			SendMessage(DataFormatCenter.createPlayerByteBufferData(ownPlayer));
		}
		
		// This call the GameClient closeConnection()
		if (this.funcCallBackCloseSession != null)
		{
			this.funcCallBackCloseSession.apply();
		}
		
		isShutDown = true;
		
		if (playerUIExecutor != null)
		{
			ThreadHandler.ShutDownAndTerminateAllThreads(playerUIExecutor, 1, TimeUnit.SECONDS, null, null);
		}
	}
}
