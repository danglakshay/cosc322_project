public class BoardUtils {
 //This file contains utility functions related to the board, such as checking if a position is valid and calculating directions.
    public static final int BOARD_SIZE = 10;
    public static final int EMPTY = 0;
    public static final int WHITE_AMAZON = 1;
    public static final int BLACK_AMAZON = 2;
    public static final int ARROW = 3;

    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    public static int[][] createExampleBoard() {
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

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

        // Place some arrows for testing
        board[5][5] = ARROW;
        board[4][4] = ARROW;
        board[5][4] = ARROW;

        return board;
    }
    
    public static void printBoard(int[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

 //The board is initialized with four white amazons, four black amazons, and a few arrows placed on the board. The constants EMPTY, WHITE_AMAZON, BLACK_AMAZON, and ARROW are used to represent the different types of cells on the board. We can adjust the placement of the pieces and the number of arrows as needed for our tests.

}