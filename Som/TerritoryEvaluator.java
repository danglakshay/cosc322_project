public class TerritoryEvaluator {
 //This file calculates the territory score, focusing on the control of the board.
    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {-1, 1}, {1, -1}
    };

    public static double calculateTerritoryScore(int[][] board, int player) {
        double territoryScore = 0;

        // Use a BFS approach to calculate the territory controlled by each amazon
        for (int i = 0; i < BoardUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardUtils.BOARD_SIZE; j++) {
                if (board[i][j] == player) {
                    boolean[][] visited = new boolean[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];
                    territoryScore += bfsTerritory(board, i, j, visited);
                }
            }
        }

        return territoryScore;
    }

    private static int bfsTerritory(int[][] board, int startX, int startY, boolean[][] visited) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startX, startY});
        visited[startX][startY] = true;
        int count = 1;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            for (int[] direction : DIRECTIONS) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                while (BoardUtils.isValidPosition(newX, newY) && board[newX][newY] == 0 && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    count++;
                    newX += direction[0];
                    newY += direction[1];
                }
            }
        }
        return count;
    }
 //The calculateTerritoryScore method calculates the territory score by iterating over the board to find the player's amazons and then using a breadth-first search (BFS) approach to calculate the territory controlled by each amazon. The bfsTerritory method performs the BFS to count the number of squares that can be reached by the amazon, which represents the territory controlled by that amazon. The total territory score is the sum of the territories controlled by all of the player's amazons.
 //This implementation considers the control of the board by calculating the number of squares that each amazon can reach, which is a basic way to assess territory control. We can further refine this method by adding weights to different squares based on their value in controlling the game.

}