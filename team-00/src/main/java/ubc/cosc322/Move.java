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
			
			
			if(allQueenTargets.size()!=0)
				System.out.println(allQueenTargets.size());

			while (!allQueenTargets.isEmpty()) {
				// Iterating through all the possible moves and removing them one by one
				int[] queenTarget = allQueenTargets.remove(0);

				// Creating a double array list of all possible arrow moves for the current queen target position
				ArrayList<int[]> allArrowTargets = allPossibleMoves(queenCurrent[0], queenCurrent[1], board);

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

		boolean isUpBlocked = false;
		boolean isDownBlocked = false;
		boolean isRightBlocked = false;
		boolean isLeftBlocked = false;
		boolean isRightUpBlocked = false;
		boolean isRightDownBlocked = false;
		boolean isLeftUpBlocked = false;
		boolean isLeftDownBlocked = false;

		for (int dist = 0; dist < 10; dist++) {
			int up = y + dist;
			int down = y - dist;
			int right = x + dist;
			int left = x - dist;
			

			if (!isUpBlocked) {
				if (up > 9 || board.getCellValue(x, up) != 0) {
					isUpBlocked = true;
				} else {
					System.out.println("hi");
					targets.add(new int[]{x, up});
				}
			}

			if (!isDownBlocked) {
				if (down < 0 || board.getCellValue(x, down) != 0) {
					isDownBlocked = true;
				} else {
					targets.add(new int[]{x, down});
				}
			}

			if (!isRightBlocked) {
				if (right > 9 || board.getCellValue(right, y) != 0) {
					isRightBlocked = true;
				} else {
					targets.add(new int[]{right, y});
				}
			}

			if (!isLeftBlocked) {
				if (left < 0 || board.getCellValue(left, y) != 0) {
					isLeftBlocked = true;
				} else {
					targets.add(new int[]{left, y});
				}
			}

			if (!isRightUpBlocked) {
				if (right > 9 || up > 9 || board.getCellValue(right, up) != 0) {
					isRightUpBlocked = true;
				} else {
					targets.add(new int[]{right, up});
				}
			}

			if (!isRightDownBlocked) {
				if (right > 9 || down < 0 || board.getCellValue(right, down) != 0) {
					isRightDownBlocked = true;
				} else {
					targets.add(new int[]{right, down});
				}
			}

			if (!isLeftUpBlocked) {
				if (left < 0 || up > 9 || board.getCellValue(left, up) != 0) {
					isLeftUpBlocked = true;
				} else {
					targets.add(new int[]{left, up});
				}
			}

			if (!isLeftDownBlocked) {
				if (left < 0 || down < 0 || board.getCellValue(left, down) != 0) {
					isLeftDownBlocked = true;
				} else {
					targets.add(new int[]{left, down});
				}
			}
		}
		
		return targets;
	}
    
}
