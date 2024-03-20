public class EndgameEvaluator {
 //This file handles endgame scenarios, including zugzwang positions and filling puzzles.
    public static double calculateEndgameScore(int[][] board, int player) {
        double endgameScore = 0;

        // Check if the player is in a zugzwang position
        if (isInZugzwang(board, player)) {
            endgameScore -= 5; // Penalize being in a zugzwang position
        }

        // Additional logic for filling puzzles could be added here

        return endgameScore;
    }

    private static boolean isInZugzwang(int[][] board, int player) {
        int opponent = player == 1 ? 2 : 1;
        boolean playerHasSafeMove = false;
        boolean opponentHasAdvantageousMove = false;

        // Check if the player has any safe moves that do not worsen their position
        for (int i = 0; i < BoardUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardUtils.BOARD_SIZE; j++) {
                if (board[i][j] == player && MobilityEvaluator.hasSafeMove(board, i, j, player)) {
                    playerHasSafeMove = true;
                    break;
                }
            }
            if (playerHasSafeMove) {
                break;
            }
        }

        // Check if the opponent has any moves that can significantly improve their position
        for (int i = 0; i < BoardUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardUtils.BOARD_SIZE; j++) {
                if (board[i][j] == opponent && MobilityEvaluator.hasAdvantageousMove(board, i, j, opponent)) {
                    opponentHasAdvantageousMove = true;
                    break;
                }
            }
            if (opponentHasAdvantageousMove) {
                break;
            }
        }

        // The player is in zugzwang if they have no safe moves and the opponent has advantageous moves
        return !playerHasSafeMove && opponentHasAdvantageousMove;
    }

 //The isInZugzwang method checks if the player has any safe moves that do not worsen their position and if the opponent has any moves that can significantly improve their position. The player is considered to be in zugzwang if they have no safe moves while the opponent has advantageous moves.


}