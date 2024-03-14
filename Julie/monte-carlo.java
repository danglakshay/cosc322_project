package Julie;
import java.util.ArrayList;
import java.util.Random;

public class monte-carlo {
    private static final int SIMULATION_COUNT = 1000; // Number of simulations to run
    private static final double EXPLORATION_FACTOR = 1.414; // Exploration factor (sqrt(2))

    public Move findBestMove(GameState state) {
        Node rootNode = new Node(null, null, state);
        for (int i = 0; i < SIMULATION_COUNT; i++) {
            Node node = select(rootNode);
            expand(node);
            GameState newState = simulate(node);
            backpropagate(node, newState.getResult(state.getCurrentPlayer()));
        }
        return rootNode.getBestChild().getMove();
    }

    private Node select(Node node) {
        while (!node.isLeaf()) {
            if (node.getUntriedMoves().size() > 0) {
                return expand(node);
            } else {
                node = node.getBestChild();
            }
        }
        return node;
    }

    private Node expand(Node node) {
        Move move = node.getUntriedMoves().remove(new Random().nextInt(node.getUntriedMoves().size()));
        GameState newState = node.getState().applyMove(move);
        Node newNode = new Node(node, move, newState);
        node.addChild(newNode);
        return newNode;
    }

    private GameState simulate(Node node) {
        GameState state = node.getState().clone();
        while (!state.isGameOver()) {
            ArrayList<Move> legalMoves = state.getLegalMoves();
            Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
            state = state.applyMove(randomMove);
        }
        return state;
    }

    private void backpropagate(Node node, int result) {
        while (node != null) {
            node.updateStats(result);
            node = node.getParent();
        }
    }

    private static class Node {
        private Node parent;
        private Move move;
        private GameState state;
        private ArrayList<Node> children;
        private int wins;
        private int visits;

        public Node(Node parent, Move move, GameState state) {
            this.parent = parent;
            this.move = move;
            this.state = state;
            this.children = new ArrayList<>();
            this.wins = 0;
            this.visits = 0;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public ArrayList<Move> getUntriedMoves() {
            return state.getLegalMoves();
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public Node getParent() {
            return parent;
        }

        public Move getMove() {
            return move;
        }

        public GameState getState() {
            return state;
        }

        public Node getBestChild() {
            double bestScore = -1;
            Node bestChild = null;
            for (Node child : children) {
                double score = (double) child.wins / child.visits + EXPLORATION_FACTOR *
                        Math.sqrt(Math.log(visits) / child.visits);
                if (score > bestScore) {
                    bestScore = score;
                    bestChild = child;
                }
            }
            return bestChild;
        }

        public void updateStats(int result) {
            visits++;
            wins += result;
        }
    }
}
