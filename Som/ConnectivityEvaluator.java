import java.util.LinkedList;
import java.util.Queue;

public class ConnectivityEvaluator {
 //This file evaluates the connectivity of the player's territory.
    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {-1, 1}, {1, -1}
    };

    public static double calculateConnectivityScore(int[][] board, int player) {
        double connectivityScore = 0;
        boolean[][] visited = new boolean[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];

        for (int i = 0; i < BoardUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardUtils.BOARD_SIZE; j++) {
                if (board[i][j] == player && !visited[i][j]) {
                    int territorySize = bfsConnectivity(board, i, j, visited, player);
                    connectivityScore += Math.pow(territorySize, 2); // Reward larger connected territories
                }
            }
        }

        return connectivityScore;
    }

    private static int bfsConnectivity(int[][] board, int startX, int startY, boolean[][] visited, int player) {
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
                if (BoardUtils.isValidPosition(newX, newY) && board[newX][newY] == player && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                    count++;
                }
            }
        }
        return count;
    }

 //The calculateConnectivityScore method calculates the connectivity score by iterating over the board to find the player's amazons and then using a breadth-first search (BFS) approach to calculate the size of each connected territory. The bfsConnectivity method performs the BFS to count the number of squares in each connected territory. The connectivity score is the sum of the squares of the sizes of all connected territories, which rewards larger connected territories.
 //This approach evaluates the connectivity of the player's territory by considering the size of each connected component of the player's amazons. Larger connected territories are rewarded more heavily, which encourages maintaining connectivity in the player's territory.

}
