package game.UtilityAndConstant;

public final class ConstantAndDefine {
	
	// ------------------------------- Game ------------------------------------
	// 16 x 16 pixels for each tile, standard size for retro(past time) 2D game
	// Each Player size
	// Each floor block size
	// Each wall block
	public final static int STANDARD_TILE_SIZE = 16;
	
	public final static int SCALE_FACTOR = 3;
	
	public final static int GAME_TILE_SIZE = STANDARD_TILE_SIZE * SCALE_FACTOR;	// 48 x 48
	public final static int PLAYER_SIZE_XY = GAME_TILE_SIZE;
	
	public final static int GAME_BOARD_COLUMN = 23;
	public final static int GAME_BOARD_ROW	= 23;
	
	public final static int UI_WIDTH = GAME_TILE_SIZE * GAME_BOARD_COLUMN;		// 960	 pixels
	public final static int UI_HEIGHT = GAME_TILE_SIZE * GAME_BOARD_ROW;		// 960   pixels
	
	public final static int PLAYER_DEFAULT_SPEED = 4;		// 4 pixel per move
	
	public final static int INIT_X_SHIFT = 100;
	public final static int INIT_Y_SHIFT = 100;
	
	public final static int FPS = 60;
	public final static long iENSURE_EACH_LOOP_IN_NS = 1_000_000; // 1 ms
	public final static int SPRITE_PLAY_FPS = 5;
	
	public enum ePLAYER
	{
		eP1, eP2, eP3, eP4, eMAX_PLAYER;
		
		public String toString()
		{
			switch (this)
			{
				case eP1:
					return "Player1";
				case eP2:
					return "Player2";
				case eP3:
					return "Player3";
				case eP4:
					return "Player4";
				default:
					throw new IllegalArgumentException();
			}
		}
		
		static public ePLAYER fromValue(int val)
		{
			switch(val)
			{
				case 0:
					return eP1;
				case 1:
					return eP2;
				case 2:
					return eP3;
				case 3:
					return eP4;
				default:
					throw new IllegalArgumentException();
			}
		}
		
		static public ePLAYER fromValue(String val)
		{
			return ePLAYER.fromValue(Integer.valueOf(val));
		}
	}
	public enum eCONTROL_KEY
	{
		eUP, eDOWN, eLEFT, eRIGHT, eENTER, eTOTAL_KEY;
		
		static public eCONTROL_KEY fromValue(int val)
		{
			switch(val)
			{
				case 0:
					return eUP;
				case 1:
					return eDOWN;
				case 2:
					return eLEFT;
				case 3:
					return eRIGHT;
				case 4:
					return eENTER;
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	
	public enum ePLAYER_SPRITE_DIR
	{
		eUP_1, eUP_2, eDOWN_1, eDOWN_2, eLEFT_1, eLEFT_2, eRIGHT_1, eRIGHT_2, eTOTAL_SPRITE_DIR;
		
		static public ePLAYER_SPRITE_DIR fromValue(Integer val)
		{
			switch(val)
			{
				case 0:
					return eUP_1;
				case 1:
					return eUP_2;
				case 2:
					return eDOWN_1;
				case 3:
					return eDOWN_2;
				case 4:
					return eLEFT_1;
				case 5:
					return eLEFT_2;
				case 6:
					return eRIGHT_1;
				case 7:
					return eRIGHT_2;
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	
	public enum eMAP
	{
		eMAP_1, eMAP_2, eTOTAL_MAP;
		
		public String toString()
		{
			switch (this)
			{
				case eMAP_1:
					return "1";
				case eMAP_2:
					return "2";
				default:
					throw new IllegalArgumentException();
			}
		}
		
		static public eMAP fromValue(Integer val)
		{
			switch(val)
			{
				case 0:
					return eMAP_1;
				case 1:
					return eMAP_2;
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	
	public enum eTILE	
	{
		ePATH, eWALL, eTREE, eFLAG, eTOTAL_TILE_TYPE;
		
		static public eTILE fromValue(String val)
		{
			return eTILE.fromValue(Integer.valueOf(val));
		}
		
		static public eTILE fromValue(Integer val)
		{
			switch(val)
			{
				case 0:
					return ePATH;
				case 1:
					return eWALL;
				case 2:
					return eTREE;
				case 3:
					return eFLAG;
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	
	
	// ------------------------------- Connection ------------------------------------
	public final static String HOST_NAME = "localhost";
	public final static int SERVER_PORT = 8080;
	public final static String ROOT_URL = "/websockets";
	public final static String API_URL = "/mazegame";
	
	// ------------------------------- Communication ------------------------------------
	// All the DATA KEY must be in 3 characters (3 bytes)
	public final static int DATA_KEY_BYTE_SIZE = 3;
	public final static String PLAYER_DATA_KEY 	= "PLY";
	public final static String COMMAND_DATA_KEY = "CMD";
	public final static String MAP_DATA_KEY		= "MAP";
	public final static String GAME_END_KEY     = "END";
	
	public final static int PLAYER_NAME_MAX_SIZE = 20;
	public final static int ENTITY_KEY_MAX_SIZE = 10;
	public final static int ENTITY_VAL_MAX_SIZE = 20;
	
	// CMD Data sub key
	public final static String ENTITY_KEY_ID = "ID";
	public final static String ENTITY_KEY_CLOSE = "CLOSE";
	public final static String ENTITY_KEY_REQUEST = "REQUEST";
	
}
