package game.Player;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;

public class PlayerGameHandler {

	private MazeGame gamePanel;
	private GameClient client;
	
	public PlayerGameHandler() 
	{	
		gamePanel = new MazeGame();
		
		// create GameClient with set call back to receive message to GameUI
		client = new GameClient((bytebuffer)->{
									gamePanel.onMessageReceived(bytebuffer);
									return null;
								});
		
		// then also Set callback to send message from GameUI to GameClient
		gamePanel.SetCallBackSendMessage((byteBuffer)->{
											try {
												client.sendMessage(byteBuffer);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											return null;
										});
		
		// Set the call back when the MazeGame success created player object after receive Server
		// provided id
		gamePanel.SetCallBackSuccessCreatePlayer(()->{
													waitCreatePlayerCheckedOneChkPt();
													return null;
												});
		
		// Set call back when the gamePanel close to close the client session
		gamePanel.SetCallBackCloseSession(()->{
												client.closeConnection();
												return null;
											});
		
		// Set call back init wait Maze Data
		gamePanel.SetCallBackInitWaitMazeData(()->{
													client.initWaitMazeData();
													return null;
												});
		
		// Set call back init wait Maze Data
		gamePanel.SetCallBackFinishReceiveMazeData(()->{
														client.finishWaitMazeData();
														return null;
													});
	}
	
	public void ConnectToServer() throws Exception
	{
		client.connectToServer();
	}
	
	private void waitCreatePlayerCheckedOneChkPt()
	{
		client.waitCreatePlayerCheckedOneChkPt();
	}
	
	public String AwaitConnectionSuccess() throws InterruptedException
	{
		client.await();
		return gamePanel.getCreatePlayerErrMessage();
	}
	
	public void AwaitReceiveMazeData() throws InterruptedException
	{
		client.awaitMazeData();
	}
	
	public void CreateGameWindow()
	{
		JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("A Maze-ing Runner - " + gamePanel.getPlayerName() + " " + gamePanel.getPlayerControlDescription());
		
		window.add(gamePanel);
		
		
		
		// Refactor the window to re-size to the panel size
		window.pack();
		
		window.setLocationRelativeTo(null); // set fixed position
		window.setVisible(true);
		
		// Add Window Close Handle
		WindowCloseHandle(window);
	}
	
	private void CloseWindow(JFrame frame)
	{
		try {
			// Call the panel close to shutdown the thread created
    		gamePanel.close();
    		frame.dispose();
		} 
		catch (InterruptedException e1)
		{
			//do nothing
		}
		catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	private void WindowCloseHandle(JFrame frame)
	{
		this.gamePanel.SetCallBackQuitGame(()->{
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			return null;
		});
		
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	CloseWindow(frame);
            }
        });
	}
	
	public void GameStart()
	{
		gamePanel.StartGame();
	}
	
	public static void main(String[] args)
	{
		boolean bIsConnectionSuccess = true;
		PlayerGameHandler playerGame = new PlayerGameHandler();
		
		// Connection to the Server
		try {
			playerGame.ConnectToServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Server Not Available\nOr Connection Fail!");
			bIsConnectionSuccess = false;
		}
		
		if (bIsConnectionSuccess)
		{
			// Wait Connection Completely
			try {
				String errMsg = playerGame.AwaitConnectionSuccess();
				
				if (!errMsg.isEmpty())
				{
					JOptionPane.showMessageDialog(null, errMsg);
					bIsConnectionSuccess = false;
				}
				else
				{
					// no error wait for Maze data
					playerGame.AwaitReceiveMazeData();
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Wait Connection Fail :" + e.getMessage());
				bIsConnectionSuccess = false;
			}
		}
		
		if (bIsConnectionSuccess)
		{
			playerGame.CreateGameWindow();
		
			playerGame.GameStart();
		}
	}
}
