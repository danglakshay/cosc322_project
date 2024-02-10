package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
public class test1 extends GamePlayer{
	
    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;

	public static void main(String[] args) {
		COSC322Test player = new COSC322Test(args[0], args[1]);
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}

	}
	
    public void COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	this.gamegui = new BaseGameGUI(this);
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	//this.gamegui = new BaseGameGUI(this);
    }

	@Override
	public void connect() {
    	gameClient = new GameClient(userName, passwd, this);	
		
	}

	@Override
	public GameClient getGameClient() {
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return  this.gamegui;
	}

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	System.out.println(messageType);
    	System.out.println(msgDetails);
    	
    	if (messageType.equals(GameMessage.GAME_STATE_BOARD)) {
			gamegui.setGameState((ArrayList<Integer>) msgDetails.get("game-state"));
		}
    	
    	if(messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
    		gamegui.updateGameState(msgDetails);
    	}

    	return true; 
	}

	@Override
	public void onLogin() {
    	System.out.println("Congratualations!!! "
    			+ "I am called because the server indicated that the login is successfully");
    	System.out.println("The next step is to find a room and join it: "
    			+ "the gameClient instance created in my constructor knows how!");
    	//System.out.println(gameClient.getRoomList().toString());
    	//gameClient.joinRoom("Okanagan Lake");
    	userName = gameClient.getUserName();
    	if(gamegui != null) {
    	gamegui.setRoomInformation(gameClient.getRoomList());
    	}
		
	}

	@Override
	public String userName() {
		return userName;
	}

}
