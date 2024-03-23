public class validOpponentMoveCheck1 {
    public boolean isMoveLegal(List<Integer> queenCurrent, List<Integer> queenTarget, List<Integer> arrowTarget) {
        // Check if the starting position has the opponent's queen
        if (getPositionValue(queenCurrent) != getOpponent()) {
            return false;
        }

        // Check if the queen's move is valid
        if (!isPathClear(queenCurrent, queenTarget)) {
            return false;
        }

        // Temporarily make the move to validate the arrow shot
        int originalValue = getPositionValue(queenCurrent);
        setPositionValue(queenCurrent, 0); // Remove the queen from the current position
        setPositionValue(queenTarget, originalValue); // Place the queen at the target position

        boolean arrowMoveValid = isPathClear(queenTarget, arrowTarget) && getPositionValue(arrowTarget) == 0;

        // Revert the board to the original state
        setPositionValue(queenTarget, 0); // Remove the queen from the target position
        setPositionValue(queenCurrent, originalValue); // Place the queen back at the current position

        return arrowMoveValid;
    }

    private boolean isPathClear(List<Integer> start, List<Integer> end) {
        int startX = start.get(0);
        int startY = start.get(1);
        int endX = end.get(0);
        int endY = end.get(1);

        int deltaX = Integer.compare(endX, startX);
        int deltaY = Integer.compare(endY, startY);

        int x = startX + deltaX;
        int y = startY + deltaY;

        while (x != endX || y != endY) {
            if (getPositionValue(Arrays.asList(x, y)) != 0) {
                return false;
            }
            x += deltaX;
            y += deltaY;
        }

        return true; // The path is clear if the loop completes without returning false
    }

}
