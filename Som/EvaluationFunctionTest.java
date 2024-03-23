
public class EvaluationFunctionTest {
	
	public static void main(String[] args) {
		/*
        // Test Case 1: Open Board
        int[][] openBoard = BoardUtils.createExampleBoard();
        testEvaluation(openBoard, "Open Board");
        
        // Test Case 2: Blocked Amazon
        int[][] blockedBoard = new int[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];
        blockedBoard[1][4] = BoardUtils.WHITE_AMAZON;
        blockedBoard[9][5] = BoardUtils.BLACK_AMAZON;
        blockedBoard[2][3] = BoardUtils.ARROW;
        blockedBoard[2][4] = BoardUtils.ARROW;
        blockedBoard[2][5] = BoardUtils.ARROW;
        blockedBoard[3][3] = BoardUtils.ARROW;
        blockedBoard[3][5] = BoardUtils.ARROW;
        blockedBoard[4][3] = BoardUtils.ARROW;
        blockedBoard[4][4] = BoardUtils.ARROW;
        blockedBoard[4][5] = BoardUtils.ARROW;
        testEvaluation(blockedBoard, "Blocked Amazon"); 
        
        
		// Test Case 3: Central Control
		int[][] centralControlBoard = new int[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];
		centralControlBoard[4][4] = BoardUtils.WHITE_AMAZON;
		centralControlBoard[4][5] = BoardUtils.BLACK_AMAZON;
		centralControlBoard[5][4] = BoardUtils.BLACK_AMAZON;
		centralControlBoard[5][5] = BoardUtils.WHITE_AMAZON;
		testEvaluation(centralControlBoard, "Central Control");
		
		// Test Case 4: Asymmetric Threats
		int[][] asymmetricBoard = new int[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];
		asymmetricBoard[2][3] = BoardUtils.WHITE_AMAZON; 
		asymmetricBoard[2][7] = BoardUtils.BLACK_AMAZON; 
		asymmetricBoard[3][2] = BoardUtils.ARROW;
		asymmetricBoard[3][3] = BoardUtils.ARROW;
		asymmetricBoard[3][4] = BoardUtils.ARROW;
		asymmetricBoard[3][5] = BoardUtils.ARROW;
		asymmetricBoard[3][6] = BoardUtils.ARROW;
		testEvaluation(asymmetricBoard, "Asymmetric Threats");
		
		// Test Case 5: Diagonal Threat
		int[][] diagonalThreatBoard = new int[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];
		diagonalThreatBoard[2][3] = BoardUtils.WHITE_AMAZON; 
		diagonalThreatBoard[4][5] = BoardUtils.BLACK_AMAZON; 
		diagonalThreatBoard[3][4] = BoardUtils.ARROW; 
		testEvaluation(diagonalThreatBoard, "Diagonal Threat");
		
		*/
		
		// Test Case 6: White's Strategic Position
		int[][] strategicPositionBoard = new int[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];
		strategicPositionBoard[3][4] = BoardUtils.WHITE_AMAZON; // White's amazon in a strategic position
		strategicPositionBoard[6][6] = BoardUtils.BLACK_AMAZON; // Black's amazon in a less strategic position
		testEvaluation(strategicPositionBoard, "White's Strategic Position");
		
    }

    private static void testEvaluation(int[][] board, String testCaseName) {
        System.out.println("Test Case: " + testCaseName);
        BoardUtils.printBoard(board);
        double scorePlayer1 = AmazonEvaluation.evaluate(board, BoardUtils.WHITE_AMAZON);
        double scorePlayer2 = AmazonEvaluation.evaluate(board, BoardUtils.BLACK_AMAZON);
        System.out.println("Score for White: " + scorePlayer1);
        System.out.println("Score for Black: " + scorePlayer2);
        System.out.println();
    }

}
