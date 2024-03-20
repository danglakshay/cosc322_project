public class MobilityEvaluator {
 //This file is responsible for calculating the mobility score, considering the quality of moves and the ability to restrict the opponent's moves.
    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {-1, 1}, {1, -1}
    };

    public static double calculateMobilityScore(int[][] board, int player) {
        double mobilityScore = 0;
        int opponent = player == 1 ? 2 : 1;

        for (int i = 0; i < BoardUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardUtils.BOARD_SIZE; j++) {
                if (board[i][j] == player) {
                    for (int[] direction : DIRECTIONS) {
                        int x = i + direction[0];
                        int y = j + direction[1];
                        while (BoardUtils.isValidPosition(x, y) && board[x][y] == 0) {
                            mobilityScore += 1; // Increase score for each possible move
                            // Check if the move blocks the opponent's amazon
                            if (isBlockingOpponent(board, x, y, opponent)) {
                                mobilityScore += 0.5; // Add additional score for blocking the opponent
                            }
                            x += direction[0];
                            y += direction[1];
                        }
                    }
                }
            }
        }
        return mobilityScore;
    }

    public static boolean hasSafeMove(int[][] board, int x, int y, int player) {
        for (int[] direction : DIRECTIONS) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (BoardUtils.isValidPosition(newX, newY) && board[newX][newY] == 0 && !isVulnerable(board, newX, newY, player)) {
                return true; // Found a safe move
            }
        }
        return false;
    }

    public static boolean hasAdvantageousMove(int[][] board, int x, int y, int player) {
        for (int[] direction : DIRECTIONS) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (BoardUtils.isValidPosition(newX, newY) && board[newX][newY] == 0 && improvesControl(board, newX, newY, player)) {
                return true; // Found an advantageous move
            }
        }
        return false;
    }

    private static boolean isBlockingOpponent(int[][] board, int x, int y, int opponent) {
        for (int[] direction : DIRECTIONS) {
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            if (BoardUtils.isValidPosition(nextX, nextY) && board[nextX][nextY] == opponent) {
                return true; // Found an opponent's amazon that can be blocked
            }
        }
        return false; // No opponent's amazon can be blocked by this move
    }

    private static boolean isVulnerable(int[][] board, int x, int y, int player) {
        // Check if the move to position (x, y) leaves the amazon vulnerable to being blocked by the opponent
        int opponent = player == 1 ? 2 : 1;
        for (int[] direction : DIRECTIONS) {
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            if (BoardUtils.isValidPosition(nextX, nextY) && board[nextX][nextY] == 0) {
                // Check if the opponent can block the amazon after the move
                for (int[] oppDirection : DIRECTIONS) {
                    int oppX = nextX + oppDirection[0];
                    int oppY = nextY + oppDirection[1];
                    if (BoardUtils.isValidPosition(oppX, oppY) && board[oppX][oppY] == opponent) {
                        return true; // The move is vulnerable to being blocked
                    }
                }
            }
        }
        return false;
    }

    private static boolean improvesControl(int[][] board, int x, int y, int player) {
    // Evaluate if the move to position (x, y) significantly improves the player's control over the board
    // For simplicity, let's assume a move improves control if it extends the reach of the player's amazons
    int controlBeforeMove = countControlledSquares(board, player);
    // Simulate the move
    board[x][y] = player;
    int controlAfterMove = countControlledSquares(board, player);
    // Revert the move
    board[x][y] = 0;
    return controlAfterMove > controlBeforeMove;
    }

    private static int countControlledSquares(int[][] board, int player) {
    int count = 0;
        for (int i = 0; i < BoardUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardUtils.BOARD_SIZE; j++) {
                if (board[i][j] == player) {
                    for (int[] direction : DIRECTIONS) {
                        int x = i + direction[0];
                        int y = j + direction[1];
                        while (BoardUtils.isValidPosition(x, y) && board[x][y] == 0) {
                            count++;
                            x += direction[0];
                            y += direction[1];
                        }
                    }
                }
            }
        }
        return count;
    }

}