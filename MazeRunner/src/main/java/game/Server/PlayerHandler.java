package game.Server;

import java.nio.ByteBuffer;

import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER;
import static game.UtilityAndConstant.ConstantAndDefine.ePLAYER.*;
import jakarta.websocket.Session;

public class PlayerHandler {
	
	private ByteBuffer playerLastData;
	private ePLAYER ePlayerID;
	private Session session;
	
	public ByteBuffer getPlayerLastData() {
		return playerLastData;
	}

	public void setPlayerLastData(ByteBuffer playerLastData) {
		this.playerLastData = playerLastData;
	}

	public ePLAYER getePlayerID() {
		return ePlayerID;
	}

	public void setePlayerID(ePLAYER ePlayerID) {
		this.ePlayerID = ePlayerID;
	}

	public Session getSession() {
		return session;
	}

	public PlayerHandler(Session session, ePLAYER id)
	{
		playerLastData = null;
		ePlayerID = id;
		this.session = session;
	}
	
	
}
