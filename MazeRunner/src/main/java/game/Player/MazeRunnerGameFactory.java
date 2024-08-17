package game.Player;

import static game.UtilityAndConstant.ConstantAndDefine.API_URL;
import static game.UtilityAndConstant.ConstantAndDefine.HOST_NAME;
import static game.UtilityAndConstant.ConstantAndDefine.ROOT_URL;
import static game.UtilityAndConstant.ConstantAndDefine.SERVER_PORT;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game.UtilityAndConstant.JPanelUtility;
import game.UtilityAndConstant.NoArgFunction;

public class MazeRunnerGameFactory {
	
	private JTextField serverAddress;
	
	public MazeRunnerGameFactory() 
	{}
	
	private static class singletonHelper
	{
		private static final MazeRunnerGameFactory instance = new MazeRunnerGameFactory();
	}
	
	private static MazeRunnerGameFactory getInstance()
	{
		return singletonHelper.instance;
	}
	// GameServer Instance singleton
	
	
	private void CreateFactoryUI()
	{
		final int panelVGap = 10;
		final int mainWidth = 300;
        final int mainHeight = 150;
        final Color bkgrdColor = Color.DARK_GRAY;
        
		// ----------------------------------------------------- The Main UI Frame Creation
        JFrame frame = new JFrame ("A Maze-ing Runner Game Factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ----------------------------------------------------- A Main Panel to Store All Components
        JPanel mainPanel = JPanelUtility.createBoxLayoutPanel(mainWidth, mainHeight, BoxLayout.Y_AXIS, bkgrdColor);
        
        // ----------------------------------------------------- Title
        final int titleHeight = 25;
        JLabel titleText = JPanelUtility.createTextLabel("A Maze-ing Runner", Color.decode("#20AD65"), new Font("Arial", Font.BOLD, titleHeight));
        // Parent
        JPanel titlePanel = JPanelUtility.createFlowLayoutPanel(FlowLayout.CENTER, 0, panelVGap, bkgrdColor);
        titlePanel.add(titleText);
        
        // After added the child component, set the desired size
        JPanelUtility.setPanelSize(titlePanel, null, titleHeight + panelVGap * 2); // set null to use the current preferredSize
        
        // add the Title Panel to Main Panel
        mainPanel.add(titlePanel);
        
        // ----------------------------------------------------- Add Entry for Server IP ------------------------------------------ //
        serverAddress = new JTextField(20);
        
        JLabel addressText = JPanelUtility.createTextLabel("Server Ip : ", Color.decode("#20AD65"), new Font("Arial", Font.BOLD, 16));
        
        // Parent
        JPanel textPanel = JPanelUtility.createFlowLayoutPanel(FlowLayout.CENTER, 0, panelVGap, bkgrdColor);
        JPanel ipPanel = JPanelUtility.createBoxLayoutPanel((mainWidth * 3) / 4, titleHeight, BoxLayout.LINE_AXIS, bkgrdColor);
        
        ipPanel.add(addressText);
        ipPanel.add(serverAddress);
        textPanel.add(ipPanel);
        
        mainPanel.add(textPanel);
        
        // ----------------------------------------------------- A Button Panel (Parent) store (Child)["New Game" Button]
        
        // add the Button Panel to Main Panel
        mainPanel.add(CreatePlayerButtonPanel());
      
       
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
	}
	
	private JPanel CreatePlayerButtonPanel()
	{
		final int panelVGap = 10;
		final int buttonWidth = 100;
        final int buttonHeight = 30;
        
        final Color bkgrdColor = Color.DARK_GRAY;
        
        // Child
        // Modify the creation flow, setup the (Child) Button first before setup the (Parent) Panel
        JButton newGameBtnPlayer = JPanelUtility.createButton("Start Game", buttonWidth, buttonHeight, Color.ORANGE, 1, bkgrdColor);
        
        JCheckBox checkBoxIsComPlayer = new JCheckBox("Com Player");
        checkBoxIsComPlayer.setBackground(bkgrdColor);
        checkBoxIsComPlayer.setForeground(Color.WHITE);
        JPanelUtility.setMargin(checkBoxIsComPlayer, 0, 0, 20, 0);
        	
        // Parent
        JPanel createPlayerPanel = JPanelUtility.createFlowLayoutPanel(FlowLayout.CENTER, 0, panelVGap, bkgrdColor);
        createPlayerPanel.add(newGameBtnPlayer);
        createPlayerPanel.add(checkBoxIsComPlayer);
        
        // After added the child component, set the desired size
        JPanelUtility.setPanelSize(createPlayerPanel, null, buttonHeight + panelVGap * 2); // set null to use the current preferredSize
        
        addActionListener(newGameBtnPlayer, ()->{
        	CreateGameBoard(checkBoxIsComPlayer);
        	return null;
        });
        
        return createPlayerPanel;
	}
	
	static public void addActionListener(JButton button, NoArgFunction<Void> funcCallBack) 
    {
		button.addActionListener(new ActionListener() 
    	{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	funcCallBack.apply();
            }
        });
        
    }
	
	private String getServerAddress()
	{
		if (!serverAddress.getText().isEmpty())
		{
			return serverAddress.getText().trim();
		}
		return HOST_NAME; // return default localhost if no entry
	}
	
	public static boolean isServerReachable(String serverUrl) 
	{
        try 
        {	
			URI	uri = new URI(serverUrl);
			
            String host = uri.getHost();
            int port = uri.getPort() == -1 ? (uri.getScheme().equals("ws") ? 80 : 443) : uri.getPort();
            
            try (Socket socket = new Socket(host, port)) 
            {
                return true;
            }
        }
        catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
	
//	public static boolean isServerReachable(String serverUrl, int timeoutMS)
//	{
//        try 
//        {
//            URL url = new URL(serverUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(timeoutMS);  // Set timeout
//            connection.setReadTimeout(timeoutMS);
//            int responseCode = connection.getResponseCode();
//            return responseCode == 200;
//        } 
//        catch (IOException e) 
//        {
//            e.printStackTrace();
//            return false;
//        }
//    }
	
	private void CreateGameBoard(JCheckBox chkBoxComPlayer)
	{
		// "ws://localhost:8080/websockets/mazegame"
		StringBuilder builder = new StringBuilder();
		builder.append("ws://").append(getServerAddress()).append(":").append(SERVER_PORT).append(ROOT_URL).append(API_URL);
		
		// Check if Server Available to allow create the client board game
		if (isServerReachable(builder.toString()))
		{
			boolean bIsConnectionSuccess = true;
			PlayerGameHandler playerGame = new PlayerGameHandler(chkBoxComPlayer.isSelected());
			
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
		else
		{
			JOptionPane.showMessageDialog(null, "Server Not Available.");
		}
	}
	
	
	public static void main(String[] arg)
	{
		getInstance().CreateFactoryUI();
	}
}
