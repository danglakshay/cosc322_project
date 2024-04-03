package ubc.cosc322;

import java.util.ArrayList;

public class gameBoard {
	public static final int BOARD_SIZE = 10;
	public static final int EMPTY = 0;
	public static final int WHITE_AMAZON = 1;
	public static final int BLACK_AMAZON = 2;
	public static final int ARROW = 3;
	static int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
	
	
	public boolean isEmpty() {
	    for (int i = 0; i < BOARD_SIZE; i++) {
	        for (int j = 0; j < BOARD_SIZE; j++) {
	            if (board[i][j] != EMPTY) {
	                return false;
	            }
	        }
	    }
	    return true;
	}

	public void createBoard() {
		// Place white amazons
		board[0][3] = WHITE_AMAZON;
		board[0][6] = WHITE_AMAZON;
		board[3][0] = WHITE_AMAZON;
		board[3][9] = WHITE_AMAZON;

		// Place black amazons
		board[6][0] = BLACK_AMAZON;
		board[6][9] = BLACK_AMAZON;
		board[9][3] = BLACK_AMAZON;
		board[9][6] = BLACK_AMAZON;
	}
	
	public void updateBoard(int[] QCurr, int[] QNew, int[] Arrow, int playerType) {
		
		board[QCurr[0]-1][QCurr[1]-1] = EMPTY;
		if(playerType == 1) {
			board[QNew[0]-1][QNew[1]-1] = BLACK_AMAZON;
		}else {
			board[QNew[0]-1][QNew[1]-1] = WHITE_AMAZON;
		}
		board[Arrow[0]-1][Arrow[1]-1] = ARROW;

	}
	

	public void printBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public int getCellValue(int i, int j) {
		return board[i][j];
    }
	

}
