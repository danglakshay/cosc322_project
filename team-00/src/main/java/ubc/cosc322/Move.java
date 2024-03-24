package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Move {
	private static int[] qCurr;
    private static int[] qNew;
    private static int[] arrow;
    private static int[][] board;

    public Move(int[] queenCurrentPosition, int[] queenNewPosition, int[] arrowPosition, int[][] board) {
        this.qCurr = queenCurrentPosition;
        this.qNew = queenNewPosition;
        this.arrow = arrowPosition;
        this.board = board;
    }
    
	public static boolean isValidPosition(int x, int y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}
	
	public static boolean isValidMove() {
		//Checks if new position of queen is in all possible moves of old position of queen
		ArrayList<int[]> allPossibleMoves = allPossibleMoves();
		if(allPossibleMoves.contains(qNew)) {
			return true;
		}
	    return false;
	}
	
	public static ArrayList<int[]> allPossibleMoves() {
		int x = qCurr[0];
		int y = qCurr[1];
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
				if (up > 9 || board[x][up] != 0) {
					isUpBlocked = true;
				} else {
					targets.add(new int[]{x, up});
				}
			}

			if (!isDownBlocked) {
				if (down < 0 || board[x][down] != 0) {
					isDownBlocked = true;
				} else {
					targets.add(new int[]{x, down});
				}
			}

			if (!isRightBlocked) {
				if (right > 9 || board[right][y] != 0) {
					isRightBlocked = true;
				} else {
					targets.add(new int[]{right, y});
				}
			}

			if (!isLeftBlocked) {
				if (left < 0 || board[left][y] != 0) {
					isLeftBlocked = true;
				} else {
					targets.add(new int[]{left, y});
				}
			}

			if (!isRightUpBlocked) {
				if (right > 9 || up > 9 || board[right][up] != 0) {
					isRightUpBlocked = true;
				} else {
					targets.add(new int[]{right, up});
				}
			}

			if (!isRightDownBlocked) {
				if (right > 9 || down < 1 || board[right][down] != 0) {
					isRightDownBlocked = true;
				} else {
					targets.add(new int[]{right, down});
				}
			}

			if (!isLeftUpBlocked) {
				if (left < 0 || up > 9 || board[left][up] != 0) {
					isLeftUpBlocked = true;
				} else {
					targets.add(new int[]{left, up});
				}
			}

			if (!isLeftDownBlocked) {
				if (left < 0 || down < 0 || board[left][down] != 0) {
					isLeftDownBlocked = true;
				} else {
					targets.add(new int[]{left, down});
				}
			}
		}

		return targets;
	}


}
