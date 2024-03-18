package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.DefaultListModel;

import sfs2x.client.entities.Room;
import ubc.cosc322.COSC322Test2.Move;
import ubc.cosc322.COSC322Test2.Node;
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
    
    private HumanPlayer player2;
    ArrayList<Integer> currentGameState;
	private int playerType;

    /**
    * The main method
    * @param args for name and passwd (current, any string would work)
    */
    public static void main(String[] args) {
        // COSC322Test player = new COSC322Test(args[0], args[1]);
        HumanPlayer player = new HumanPlayer();

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
	        int[] Arrow = (int[]) msgDetails.get(AmazonsGameMessage.ARROW_POS); // Assuming arrow positions are provided
	        Move move = new Move(QCurr, QNew, Arrow);
	        
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
				if (player2 != null && player2.getGameGUI() != null) {
	                player2.getGameGUI().setGameState(currentGameState);
	            }
			}
			
			
	}


    private void handleOpponentMove(Move move) {
    	Node rootNode = new Node(null, null, currentGameState);
    	int simCount = 3;
    	
    	//Check if opponent made valid move

		// Perform Monte Carlo Tree Search (MCTS) to select the best move
		for (int i = 0; i < simCount; i++) {
			// Start from the root node and select child nodes until a leaf node is reached
			Node node = rootNode;
			while (!node.isLeaf()) {
				node = node.selectBestChild(); // Use UCB1 to select the best child node
			}
			// Now, node is a leaf node, expand it by selecting a random untried move
			ArrayList<Move> untriedMoves = node.getUntriedMoves(currentGameState, playerType);
			if (!untriedMoves.isEmpty()) {
				Move randomMove = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
				ArrayList<Integer> newState = node.simulateMove(currentGameState, randomMove);
				Node childNode = new Node(node, randomMove, newState);
				node.addChild(childNode);
			}
			// Simulate a game from this leaf node to get a result

			// Backpropagate the result to update the statistics of ancestor nodes
			backpropagate(node, result);
		}

		// After MCTS, select the best child node based on the number of visits
		Node bestChild = rootNode.getBestChild();
        double explorationFactor = 1.414;
        msgDetails.get(GameMessage.GAME_ACTION_MOVE);

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


    private static class Node {
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

        // Get the legal moves excluding positions where an arrow is present
        public ArrayList<Move> getUntriedMoves(ArrayList<Integer> state, ArrayList<Integer> arrowPos, int player) {
            ArrayList<Move> legalMoves = new ArrayList<>();
            // Get all possible moves
            ArrayList<Move> allMoves = this.getAllPossibleMoves(state, player);
            // Check if each move is valid and add it to the legal moves list
            for (Move move : allMoves) {
                if (move.isValidMove(arrowPos)) {
                    legalMoves.add(move);
                }
            }

            return legalMoves;
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

        private static final double EXPLORATION_FACTOR = Math.sqrt(2);

        // Select the best child node based on the UCB1 formula
        public Node selectBestChild() {
            double bestScore = Double.NEGATIVE_INFINITY;
            Node bestChild = null;
            for (Node child : children) {
                double explorationTerm = Math.sqrt(Math.log(visits) / child.visits);
                double score = (double) child.wins / child.visits + EXPLORATION_FACTOR * explorationTerm;
                if (score > bestScore) {
                    bestScore = score;
                    bestChild = child;
                }
            }
            return bestChild;
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

        // Assume that the board size is represented by variables ROWS and COLS
        private static final int ROWS = 10;
        private static final int COLS = 10;
        // Define EMPTY as a constant representing an empty position on the board
        private static final int EMPTY = 0;

        public ArrayList<Move> getAllPossibleMoves(ArrayList<Integer> state, int player) {
            ArrayList<Move> possibleMoves = new ArrayList<>();

            // Iterate over all positions on the board
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    // Check if the current position is occupied by the current player
                    if (state.get(row * COLS + col) == getPlayerValue(state, player)) {
                        // Check if the queen can move from this position
                        // For Amazons, queens can move in any direction until they hit another piece or the edge of the board
                        for (int dRow = -1; dRow <= 1; dRow++) {
                            for (int dCol = -1; dCol <= 1; dCol++) {
                                // Skip the case where both dRow and dCol are 0, which represents staying in the same position
                                if (dRow == 0 && dCol == 0) {
                                    continue;
                                }
                                // Check if the queen can move in this direction until it's blocked
                                int newRow = row + dRow;
                                int newCol = col + dCol;
                                while (isValidPosition(newRow, newCol)) {
                                    // Check if the new position is empty
                                    if (isEmptyPosition(state, newRow, newCol)) {
                                        // Add this move to the list of possible moves
                                        possibleMoves.add(new Move(new int[]{row, col}, new int[]{newRow, newCol}, new int[]{0, 0}));
                                    } else {
                                        // The queen is blocked by another piece, so stop checking in this direction
                                        break;
                                    }
                                    // Move to the next position in the same direction
                                    newRow += dRow;
                                    newCol += dCol;
                                }
                            }
                        }
                    }
                }
            }

            return possibleMoves;
        }


        // Define getPlayerValue method
        private int getPlayerValue(ArrayList<Integer> state, int player) {
            return (player == 1) ? 1 : 2; // Assuming player 1 is represented by 1 and player 2 is represented by 2 in the state
        }


        // Helper method to check if a position is valid (within the bounds of the board)
        private boolean isValidPosition(int row, int col) {
            return row >= 0 && row < ROWS && col >= 0 && col < COLS;
        }

        // Helper method to check if a position is empty (not occupied by any piece)
        public boolean isEmptyPosition(ArrayList<Integer> state, int row, int col) {
            // Check if the position is within the bounds of the board
            if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
                return false; // Position is out of bounds
            }
            // Check if the position is empty
            return state.get(row * COLS + col) == EMPTY;
        }
    }

    private static class Move {
        private final int[] QCurr;
        private final int[] QNew;
        private final int[] Arrow;
        private static final int BOARD_SIZE = 10; // Assuming a 10x10 game board


        public Move(int[] QCurr, int[] QNew, int[] Arrow) {
            this.QCurr = QCurr;
            this.QNew = QNew;
            this.Arrow = Arrow;
        }
        
        private boolean isValidPosition(int row, int col) {
            return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
        }

        // Check if a position is empty (not occupied by any piece)
        private boolean isEmptyPosition(ArrayList<Integer> state, int row, int col) {
            if (!isValidPosition(row, col)) {
                return false; // Position is out of bounds
            }	
            // Check if the position is empty (represented by 0 in the state)
            return state.get(row * BOARD_SIZE + col) == 0;
        }

        public boolean isValidMove(ArrayList<Integer> arrowPos) {
            // Check if the new queen position overlaps with any arrow position
            for (int i = 0; i < arrowPos.size(); i += 2) {
                int arrowRow = arrowPos.get(i);
                int arrowCol = arrowPos.get(i + 1);
                if (QNew[0] == arrowRow && QNew[1] == arrowCol) {
                    return false; // Queen's new position overlaps with arrow position
                }
            }
            return true; // Move is valid
        }
    }

    } //end of class
