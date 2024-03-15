
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;

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
			ArrayList<Integer> gameS = (ArrayList<Integer>) msgDetails.get("game-state");
			System.out.println("Game Board: " + gameS);
			
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
	
	
	private void handleOpponentMove(Map<String, Object> msgDetails) {
		int simCount = 1000;
		double explorationFactor = 1.414;
		msgDetails.get(AmazonsGameMessage.)
		
	}
	
	

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
	
	
	private static class Node{
		private Node parent;
        private Move move;
        private ArrayList<Integer> state;
        private ArrayList<Node> children;
        private int wins;
        private int visits;

        public Node(Node parent, Move move, ArrayList<Integer> state) {
            this.parent = parent;
            this.move = move;
            this.state = state;
            this.children = new ArrayList<>();
            this.wins = 0;
            this.visits = 0;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public ArrayList<Move> getUntriedMoves() {
            return state.getLegalMoves();
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public Node getParent() {
            return parent;
        }

        public Move getMove() {
            return move;
        }

        public ArrayList<Integer> getState() {
            return state;
        }

        public Node getBestChild() {
            double bestScore = -1;
            Node bestChild = null;
            for (Node child : children) {
                double score = (double) child.wins / child.visits + EXPLORATION_FACTOR *
                        Math.sqrt(Math.log(visits) / child.visits);
                if (score > bestScore) {
                    bestScore = score;
                    bestChild = child;
                }
            }
            return bestChild;
        }

        public void updateStats(int result) {
            visits++;
            wins += result;
        }
    }
		
	
	
	private static class Move {
	    private final int[] QCurr;
	    private final int[] QNew;
	    private final int[] Arrow;

	    public Move(int[] QCurr, int[] QNew, int[] Arrow) {
	        this.QCurr = QCurr;
	        this.QNew = QNew;
	        this.Arrow = Arrow;
	    }

	    
	    
	}

	
	
	}//end of class