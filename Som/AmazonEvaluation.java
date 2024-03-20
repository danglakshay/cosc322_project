public class AmazonEvaluation {
 //This file contains the main evaluation function and integrates all the components.
    private int[][] board;

    public AmazonEvaluation(int[][] board) {
        this.board = board;
    }

    public double evaluate(int player) {
        double mobilityScore = MobilityEvaluator.calculateMobilityScore(board, player);
        double territoryScore = TerritoryEvaluator.calculateTerritoryScore(board, player);
        double connectivityScore = ConnectivityEvaluator.calculateConnectivityScore(board, player);
        double strategicImportanceScore = StrategicImportanceEvaluator.calculateStrategicImportanceScore(board, player);
        double endgameScore = EndgameEvaluator.calculateEndgameScore(board, player);

        return mobilityScore + territoryScore + connectivityScore + strategicImportanceScore + endgameScore;
    }

    public static void main(String[] args) {
        int[][] exampleBoard = BoardUtils.createExampleBoard();
        AmazonEvaluation evaluation = new AmazonEvaluation(exampleBoard);
        double score = evaluation.evaluate(1); // Evaluate the position for player 1 (white)
        System.out.println("Evaluation score for player 1: " + score);
    }
}