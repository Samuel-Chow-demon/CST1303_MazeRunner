package game.Player;

import static game.UtilityAndConstant.ConstantAndDefine.API_URL;
import static game.UtilityAndConstant.ConstantAndDefine.HOST_NAME;
import static game.UtilityAndConstant.ConstantAndDefine.ROOT_URL;
import static game.UtilityAndConstant.ConstantAndDefine.SERVER_PORT;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

import game.UtilityAndConstant.UniFunction;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ClientEndpoint
public class GameClient {
	
	private Session session;
	private CountDownLatch latch;
	private UniFunction<ByteBuffer, Void> funcCallBackOnMessage;
	
	final int CONNECT_TO_CREATE_CHECK_POINT = 2;
	
	private CountDownLatch latchWaitMapData;
	
	public GameClient(UniFunction<ByteBuffer, Void> func)
	{
		this.funcCallBackOnMessage = func;
	}

    public void connectToServer() throws Exception 
    {
    	// Set that need to wait until the onOpen is called that ensure connection established
    	latch = new CountDownLatch(CONNECT_TO_CREATE_CHECK_POINT);
    	
    	StringBuilder uri = new StringBuilder();
    	
    	// "ws://localhost:8080/websockets/mazegame"
    	uri.append("ws://").append(HOST_NAME).append(":").append(SERVER_PORT).append(ROOT_URL).append(API_URL);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        
        // This is a blocking operation
        container.connectToServer(this, new URI(uri.toString()));
    }
    
    public void waitCreatePlayerCheckedOneChkPt()
    {
    	latch.countDown(); // Drop down the flag 
    }

    @OnOpen
    public void onOpen(Session session) 
    {
        this.session = session;
        System.out.println("Connected to server");
        
        waitCreatePlayerCheckedOneChkPt(); // Drop down the flag one time
    }
    
    // Method provide blocking
    public void await() throws InterruptedException {
        latch.await();
    }
    
    // For Maze Data Handshake
    public void initWaitMazeData()
    {
    	latchWaitMapData = new CountDownLatch(1);
    }
    public void awaitMazeData() throws InterruptedException
    {
    	latchWaitMapData.await();
    }
    public void finishWaitMazeData() 
    {
    	latchWaitMapData.countDown(); // Drop down the flag 
    }

    @OnMessage
    public void onMessage(ByteBuffer data)
    {
    	this.funcCallBackOnMessage.apply(data);
        //System.out.println("Received: " + message);
    }

    @OnClose
    public void onClose() 
    {
        System.out.println("Connection closed");
    }

    public void sendMessage(ByteBuffer data) throws IOException 
    {
        if (session != null && session.isOpen()) 
        {
        	// Send By Binary Format
        	// data format in byte - "Player1,id,x,y,speed,isWin,isDie"
            session.getBasicRemote().sendBinary(data);
        } 
        else 
        {
            System.out.println("Session is closed. Cannot send message.");
        }
    }
    
    public void closeConnection()
    {
    	if (this.session != null)
    	{
    		try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Client closing connection"));
            } catch (IOException e) {
                System.err.println("Error closing the connection: " + e.getMessage());
            }
    	}
    }
}
