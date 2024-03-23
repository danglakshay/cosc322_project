public class validOpponentMoveCheck2 {
    private boolean isValidOpponentMove(ArrayList<Integer> QCurr, ArrayList<Integer> QNew, ArrayList<Integer> Arrow, ArrayList<Integer> gameState) {
        // Check if start, end, and arrow positions are valid (within the board and the correct path)
        if (!isPathClear(QCurr, QNew, gameState) || !isPathClear(QNew, Arrow, gameState)) {
            return false;
        }

        // Check if the starting position has an opponent's queen and the ending position is empty
        int startIdx = QCurr.get(0) * 11 + QCurr.get(1);
        int endIdx = QNew.get(0) * 11 + QNew.get(1);
        int arrowIdx = Arrow.get(0) * 11 + Arrow.get(1);

        // Assuming the opponent's queen is marked with a '2' and empty squares with a '0'
        if (gameState.get(startIdx) != 2 || gameState.get(endIdx) != 0) {
            return false;
        }

        // Check if the arrow lands in an empty square
        if (gameState.get(arrowIdx) != 0) {
            return false;
        }

        return true;
    }

    private boolean isPathClear(ArrayList<Integer> start, ArrayList<Integer> end, ArrayList<Integer> gameState) {
        int dx = Integer.compare(end.get(1), start.get(1));
        int dy = Integer.compare(end.get(0), start.get(0));

        int x = start.get(1) + dx;
        int y = start.get(0) + dy;

        while (x != end.get(1) || y != end.get(0)) {
            if (gameState.get(y * 11 + x) != 0) { // Assuming '0' represents an empty square
                return false;
            }
            x += dx;
            y += dy;
        }

        return true;
    }

}
