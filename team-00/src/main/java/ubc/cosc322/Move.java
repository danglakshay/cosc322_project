package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Move {
	public int[] qCurr;
    public int[] qNew;
    public int[] arrow;

    public Move(int[] qCurr, int[] qNew, int[] arrow) {
        this.qCurr = qCurr;
        this.qNew = qNew;
        this.arrow = arrow;
    }
    
	public static ArrayList<Move> getActions(gameBoard board) {
		ArrayList<Move> list = new ArrayList<>();
		
		ArrayList<int[]> allPlayerQueens = allPlayerQueens(board);

		while (!allPlayerQueens.isEmpty()) {
			int[] queen1 = allPlayerQueens.remove(0);
			ArrayList<int[]> allQueenMoves = allPossibleMoves(queen1[0], queen1[1], board);

			while (!allQueenMoves.isEmpty()) {
				int[] queenTarget = allQueenMoves.remove(0);
				ArrayList<int[]> allArrowMoves = allPossibleMoves(queenTarget[0], queenTarget[1], board);
				allArrowMoves.add(queen1);

				while (!allArrowMoves.isEmpty()) {
					int[] arrowTarget = allArrowMoves.remove(0);
					Move newAction = new Move(queen1, queenTarget, arrowTarget);
					list.add(newAction);
				}
			}
		}
		return list;
	}
	private static ArrayList<int[]> allPlayerQueens(gameBoard board) {
		ArrayList<int[]> playerQueens = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j <10; j++) {
				int[] position = new int[]{i, j};
				if (board.getCellValue(i, j) == board.playerTypeLocal) {
					playerQueens.add(position);
				}
			}
		}
		//Returns current queen positions of specified playerType
		return playerQueens;
	}
    
	public static boolean isValidPosition(int x, int y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}
	
	public static ArrayList<int[]> allPossibleMoves(int x, int y , gameBoard board) {
	    ArrayList<int[]> possibleMoves = new ArrayList<>();

	    //Checks moves diagonally, horizontally, and vertically until blocked
	    for (int up = y + 1; up < 10; up++) {
	        if (board.getCellValue(x, up) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{x, up});
	        }
	    }

	    for (int down = y - 1; down >= 0; down--) {
	        if (board.getCellValue(x, down) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{x, down});
	        }
	    }

	    for (int right = x + 1; right < 10; right++) {
	        if (board.getCellValue(right, y) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{right, y});
	        }
	    }
	    
	    for (int left = x - 1; left >= 0; left--) {
	        if (board.getCellValue(left, y) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{left, y});
	        }
	    }

	    for (int rightUpX = x + 1, rightUpY = y + 1; rightUpX < 10 && rightUpY < 10; rightUpX++, rightUpY++) {
	        if (board.getCellValue(rightUpX, rightUpY) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{rightUpX, rightUpY});
	        }
	    }

	    for (int rightDownX = x + 1, rightDownY = y - 1; rightDownX < 10 && rightDownY >= 0; rightDownX++, rightDownY--) {
	        if (board.getCellValue(rightDownX, rightDownY) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{rightDownX, rightDownY});
	        }
	    }

	    for (int leftUpX = x - 1, leftUpY = y + 1; leftUpX >= 0 && leftUpY < 10; leftUpX--, leftUpY++) {
	        if (board.getCellValue(leftUpX, leftUpY) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{leftUpX, leftUpY});
	        }
	    }
	    
	    for (int leftDownX = x - 1, leftDownY = y - 1; leftDownX >= 0 && leftDownY >= 0; leftDownX--, leftDownY--) {
	        if (board.getCellValue(leftDownX, leftDownY) != 0) {
	            break;
	        } else {
	            possibleMoves.add(new int[]{leftDownX, leftDownY});
	        }
	    }

	    return possibleMoves;
	}
    
}
