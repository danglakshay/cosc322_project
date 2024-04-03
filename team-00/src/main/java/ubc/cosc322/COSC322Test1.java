package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;
public class COSC322Test1 extends GamePlayer{
	
    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
    
    public gameBoard game = new gameBoard();
    
    ArrayList<Integer> currentGameState;
	public int playerType;

	public static void main(String[] args) {
		COSC322Test1 player = new COSC322Test1("hi", "hi");
		//HumanPlayer player2 = new HumanPlayer();
		//player2.Go();
    	
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
	
    public COSC322Test1(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;  	
    	this.gamegui = new BaseGameGUI(this);
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
		switch(messageType) {
		case GameMessage.GAME_STATE_BOARD: handleBoardMessage(msgDetails); break;
		case GameMessage.GAME_ACTION_START: handleGameStart(msgDetails); break;
		case GameMessage.GAME_ACTION_MOVE: handleGameMove(msgDetails); break;
		}
		return true;
	}


	private void handleGameMove(Map<String, Object> msgDetails) {
		if (gamegui != null) {
			gamegui.updateGameState(msgDetails);
		}
		
		int[] QCurr = (int[]) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
        int[] QNew = (int[]) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
        int[] Arrow = (int[]) msgDetails.get(AmazonsGameMessage.ARROW_POS);
        game.updateBoard(QCurr, QNew, Arrow, playerType);
        Move move = new Move(QCurr, QNew, Arrow, game, playerType);
        
        //Checks if opponents move is valid
        if(move.isValidMove()) {
        	game.printBoard();
        	System.out.println("VAlID MOVE -- CALCULATING NEXT MOVE");
        	monteCarlo player = new monteCarlo();
        	Move newMove = player.move();
        	game.updateBoard(newMove.getQCurr(), newMove.getQNew(), newMove.getArrow(),newMove.getPlayerType());
            game.printBoard();
            gamegui.updateGameState(convert(newMove.getQCurr()), convert(newMove.getQNew()),convert(newMove.getArrow()));
            gameClient.sendMoveMessage(convert(newMove.getQCurr()), convert(newMove.getQNew()), convert(newMove.getArrow()));
        }else {
        	System.out.println("INVALID MOVE -- WE WIN SUCKERS");
        }
		
	}

	private void handleGameStart(Map<String, Object> msgDetails) {
		System.out.println("Game Start: Black Played by " + msgDetails.get(AmazonsGameMessage.PLAYER_BLACK));
        System.out.println("Game Start: White Played by " + msgDetails.get(AmazonsGameMessage.PLAYER_WHITE));
        System.out.println("Timer Started on Black");

		if (((String) msgDetails.get("player-black")).equals(this.userName())) {
			playerType = 1;
		}else {
			playerType = 2;
		}
		
	}

	private void handleBoardMessage(Map<String, Object> msgDetails) {
		if (gamegui != null) {
			currentGameState = (ArrayList<Integer>) msgDetails.get("game-state");
			gamegui.setGameState(currentGameState);
		}
		if(game.isEmpty())
			game.createBoard();
		
	}
	
	private ArrayList<Integer> convert(int[] array) {
		ArrayList<Integer> send = new ArrayList<Integer>();
		send.add(array[0]);
		send.add(array[1]);
		return send;
	}
	
	

	@Override
	public void onLogin() {
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