package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Move {
	public int[] qCurr;
    public int[] qNew;
    public int[] arrow;

    public Move(int[] queenCurrentPosition, int[] queenNewPosition, int[] arrowPosition) {
        this.qCurr = queenCurrentPosition;
        this.qNew = queenNewPosition;
        this.arrow = arrowPosition;
    }
    
	public static ArrayList<Move> getActions(gameBoard board) {
		ArrayList<Move> list = new ArrayList<>();
		// Double array list initialized to store queen current positions
		
		ArrayList<int[]> allQueens = getAllQueenCurrents(board);

		while (!allQueens.isEmpty()) {
			// Iterating through the allQueens arrayList removing them one by one.
			int[] queenCurrent = allQueens.remove(0);

			// Creating a double array list of all possible moves for the current queen.
			ArrayList<int[]> allQueenTargets = allPossibleMoves(queenCurrent[0], queenCurrent[1], board);

			while (!allQueenTargets.isEmpty()) {
				// Iterating through all the possible moves and removing them one by one
				int[] queenTarget = allQueenTargets.remove(0);

				// Creating a double array list of all possible arrow moves for the current queen target position
				ArrayList<int[]> allArrowTargets = allPossibleMoves(queenTarget[0], queenTarget[1], board);

				// Add the queen's original position as a potential arrow target
				allArrowTargets.add(queenCurrent);

				while (!allArrowTargets.isEmpty()) {
					// Iterating through all the arrow target locations and removing them.
					int[] arrowTarget = allArrowTargets.remove(0);

					// Adding the current arrow target, the queen target position, and the current queen to the list of AmazonsActions
					Move newAction = new Move(queenCurrent, queenTarget, arrowTarget);
					list.add(newAction);
				}
			}
		}
		return list;
	}
	private static ArrayList<int[]> getAllQueenCurrents(gameBoard board) {
		ArrayList<int[]> queenCurrents = new ArrayList<>();
		// Iterating through the entire board finding each queen position.
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j <10; j++) {
				int[] position = new int[]{i, j};
				// The queen position value can be 1 or 2 depending on if our queens are white or black.
				if (board.getCellValue(i, j) == board.playerTypeLocal) {
					queenCurrents.add(position);
				}
			}
		}
		return queenCurrents;
	}
    
	public static boolean isValidPosition(int x, int y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}
	
//	public boolean isValidMove() {
//		//Checks if new position of queen is in all possible moves of old position of queen
//		ArrayList<int[]> allPossibleQueenMoves = allPossibleMoves(getQCurr()[0],getQCurr()[1],board);
//		if(allPossibleQueenMoves.contains(qNew)) {
//			board.updateBoard(qCurr, qNew, arrow, board.playerType);
//			ArrayList<int[]> allPossibleArrowMoves = allPossibleMoves(getQNew()[0],getQNew()[1],board);
//			if(allPossibleArrowMoves.contains(arrow)){
//				return true;
//			}else {
//				return false;
//			}
//		}
//	    return false;
//	}
	
	public static ArrayList<int[]> allPossibleMoves(int x, int y , gameBoard board) {
	    ArrayList<int[]> targets = new ArrayList<>();

	    // Check upward direction
	    for (int up = y + 1; up < 10; up++) {
	        if (board.getCellValue(x, up) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{x, up});
	        }
	    }

	    // Check downward direction
	    for (int down = y - 1; down >= 0; down--) {
	        if (board.getCellValue(x, down) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{x, down});
	        }
	    }

	    // Check right direction
	    for (int right = x + 1; right < 10; right++) {
	        if (board.getCellValue(right, y) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{right, y});
	        }
	    }

	    // Check left direction
	    for (int left = x - 1; left >= 0; left--) {
	        if (board.getCellValue(left, y) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{left, y});
	        }
	    }

	    // Check right-up direction
	    for (int rightUpX = x + 1, rightUpY = y + 1; rightUpX < 10 && rightUpY < 10; rightUpX++, rightUpY++) {
	        if (board.getCellValue(rightUpX, rightUpY) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{rightUpX, rightUpY});
	        }
	    }

	    // Check right-down direction
	    for (int rightDownX = x + 1, rightDownY = y - 1; rightDownX < 10 && rightDownY >= 0; rightDownX++, rightDownY--) {
	        if (board.getCellValue(rightDownX, rightDownY) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{rightDownX, rightDownY});
	        }
	    }

	    // Check left-up direction
	    for (int leftUpX = x - 1, leftUpY = y + 1; leftUpX >= 0 && leftUpY < 10; leftUpX--, leftUpY++) {
	        if (board.getCellValue(leftUpX, leftUpY) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{leftUpX, leftUpY});
	        }
	    }

	    // Check left-down direction
	    for (int leftDownX = x - 1, leftDownY = y - 1; leftDownX >= 0 && leftDownY >= 0; leftDownX--, leftDownY--) {
	        if (board.getCellValue(leftDownX, leftDownY) != 0) {
	            break;
	        } else {
	            targets.add(new int[]{leftDownX, leftDownY});
	        }
	    }

	    return targets;
	}
    
}
