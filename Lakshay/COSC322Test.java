
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
* An example illustrating how to implement a GamePlayer
* @author Yong Gao (yong.gao@ubc.ca)
* Jan 5, 2021
*
*/
public class COSC322Test extends GamePlayer{

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;
	
	private String userName = null;
	private String passwd = null;
	
	
	/**
	* The main method
	* @param args for name and passwd (current, any string would work)
	*/
	public static void main(String[] args) {
		// COSC322Test player = new COSC322Test(args[0], args[1]);
		HumanPlayer player = new HumanPlayer();
		HumanPlayer player2 = new HumanPlayer();
		
		player2.Go();
		
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
	
	/**
	* Any name and passwd
	* @param userName
	* @param passwd
	*/
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;
		
		//To make a GUI-based player, create an instance of BaseGameGUI
		//and implement the method getGameGUI() accordingly
		this.gamegui = new BaseGameGUI(this);
	}
	
	
	
	@Override
	public void onLogin() {
		// System.out.println("Congratualations!!! "
		// + "I am called because the server indicated that the login is successfully");
		// System.out.println("The next step is to find a room and join it: "
		// + "the gameClient instance created in my constructor knows how!");
		//
		// List<Room> roomList = gameClient.getRoomList();
		// gameClient.joinRoom(roomList.get(0).getName());
		
		userName = gameClient.getUserName();
		if(gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
	}
	
	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		//This method will be called by the GameClient when it receives a game-related message
		//from the server.
		
		//For a detailed description of the message types and format,
		//see the method GamePlayer.handleGameMessage() in the game-client-api document.
		
		if(messageType.equals(GameMessage.GAME_STATE_BOARD)){
			ArrayList<Integer> gameS = (ArrayList<Integer>) msgDetails.get("game-state");
			System.out.println("Game Board: " + gameS);
			gamegui.setGameState(gameS);
		}
		else if(messageType.equals(GameMessage.GAME_ACTION_START)){
		
		System.out.println("Game Start: Black Played by " + msgDetails.get(AmazonsGameMessage.PLAYER_BLACK));
		System.out.println("Game Start: White Played by " + msgDetails.get(AmazonsGameMessage.PLAYER_WHITE));
		System.out.println("Timer Started on Black");
		
		if(((String) msgDetails.get("player-black")).equals(this.userName())){
		 System.out.println("Game Start: " + msgDetails.get("player-black"));
		}
		
		}
		else if(messageType.equals(GameMessage.GAME_ACTION_MOVE)){
			System.out.println(msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR));
			System.out.println("MESSAGE DETAILS  -------------------------------------");
			
			gamegui.updateGameState(msgDetails);
//			handleOpponentMove(msgDetails);
		}
		else if(messageType.equals("user-count-change")) {
			//DefaultListModel<String> m = new DefaultListModel<>();
			//gameClient.getRoomList().forEach(room -> {m.addElement(room.getName() + "(" + room.getUserCount() + room.getSpectatorCount() + ")");});
			//gamegui.setRoomInformation(m);
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
		return true;
	}
	
	
//	private void handleOpponentMove(Map<String, Object> msgDetails) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public String userName() {
		return userName;
	}
	
	@Override
	public GameClient getGameClient() {
	// TODO Auto-generated method stub
		return this.gameClient;
	}
	
	@Override
	public BaseGameGUI getGameGUI() {
	// TODO Auto-generated method stub
		return this.gamegui;
	}
	
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		gameClient = new GameClient(userName, passwd, this);
	}
	
	
	}//end of class