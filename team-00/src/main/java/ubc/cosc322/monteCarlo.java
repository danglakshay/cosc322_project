package ubc.cosc322;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class monteCarlo {

	protected gameBoard board;

	private final long MAX_RUNTIME = 5000;
	private final int NUM_THREADS = 4;


	private TreeNode root;
	
	public monteCarlo(gameBoard board) {
		this.board = board;
	}

	protected Move move() {
		root = new TreeNode(board);

		long endTime = System.currentTimeMillis() + MAX_RUNTIME;
		int iterations = 0;

		// Expand initial root
		root.expand();
		ArrayList<TreeNode> rootChildren = root.children;

		Thread[] threads = new Thread[NUM_THREADS];
		MonteCarloRunnable[] runnables = new MonteCarloRunnable[NUM_THREADS];

		// Split iterations across threads
		int threadChildCount = rootChildren.size() / NUM_THREADS;
		int extraChildCount = rootChildren.size() % NUM_THREADS;
		for (int threadIdx = 0; threadIdx < NUM_THREADS; threadIdx++) {
			TreeNode threadRoot = new TreeNode(root.state);

			// Update this thread's root with its section of the children
			int startIdx = threadChildCount * threadIdx;
			int endIdx = threadChildCount * (threadIdx + 1);
			threadRoot.children = new ArrayList<>(rootChildren.subList(startIdx, endIdx));
			System.out.println("From " + startIdx + " to " + endIdx);

			// Give the first thread any extra children (due to integer rounding)
			if (threadIdx == 0) {
				startIdx = rootChildren.size() - extraChildCount;
				endIdx = rootChildren.size();
				threadRoot.children.addAll(new ArrayList<>(rootChildren.subList(startIdx, endIdx)));
				System.out.println("EXTRA: From " + startIdx + " to " + endIdx);
			}

			// Start the threads
			MonteCarloRunnable threadRunnable = new MonteCarloRunnable(threadRoot, endTime);
			runnables[threadIdx] = threadRunnable;

			Thread thread = new Thread(threadRunnable);
			threads[threadIdx] = thread;
			thread.start();
		}

		// Sync all threads and wait for completion
		for (int i = 0; i < NUM_THREADS; i++) {
			try {
				threads[i].join();
				iterations += runnables[i].iterations;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("***** TOTAL ITERATIONS: " + iterations + " *****");

		root = getBestMove(root);
		Move action = root.getAction();
		
		return action; 
		//sendMove(action.queenCurrent, action.queenTarget, action.arrowTarget);
	}
	
	private TreeNode getBestMove(TreeNode root) {
		int maxWins = -1;
		TreeNode best = null;

		for (TreeNode node : root.children) {
			// Since wins belong the to state/player, not the action/move, the wins for a given node
			// actually represent the wins of the next player moving from that node. Therefore, we
			// calculate the opponent's losses as our comparison value for the root's children.
			int wins = node.getVisits() - node.getWins();
			if (wins > maxWins) {
				maxWins = wins;
				best = node;
			}
		}

		return best;
	}
	
	private class MonteCarloRunnable implements Runnable {
		TreeNode root;
		long endTime;
		public int iterations;

		public MonteCarloRunnable(TreeNode root, long endTime) {
			this.root = root;
			this.endTime = endTime;
			iterations = 0;
		}

		@Override
		public void run() {
			while (System.currentTimeMillis() < endTime) {
				TreeNode current = getMaxLeaf(root);
				TreeNode child = current.expand();

				// Check if we reached a terminal state while expanding
				if (child == null) {
					backpropagate(current,current.state.getOpponent());
					continue;
				}

				int winner = playthrough(child);
				backpropagate(child, winner);

				iterations++;
			}
		}
	}
	
	private int playthrough(TreeNode current) {
		gameBoard state = current.state.copy();
		int winner = -1;

		while (winner < 0) {
			ArrayList<Move> actions = Move.getActions(state);

			// Check win conditions
			if (actions.size() == 0) {
				return state.getOpponent();
			}

			// Pick a random move
			int moveIndex = (int) (ThreadLocalRandom.current().nextDouble() * actions.size());
			Move move = actions.get(moveIndex);

			// Apply the selected move to the state
			state.updateBoard(move.qCurr, move.qNew, move.arrow);
			state.playerTypeLocal = state.getOpponent();
		}

		return winner;
	}
	
	private void backpropagate(TreeNode current, int winner) {
		while (current != null) {
			if (current.state.playerTypeLocal == winner) {
				current.wins++;
			}
			current.visits++;
			current = current.parent;
		}
	}
	
	private TreeNode getMaxLeaf(TreeNode root) {
		TreeNode current = root;

		while (!current.children.isEmpty()) {
			double maxUCB = Double.MIN_VALUE;
			TreeNode maxChild = null;

			for (TreeNode child : current.children) {
				double ucb = child.getUCB();
				if (ucb > maxUCB) {
					maxUCB = ucb;
					maxChild = child;
				}
			}

			current = maxChild;
		}

		return current;
	}

}

class TreeNode {
	gameBoard state;
	Move action;
	int wins = 0;
	int visits = 0;
	TreeNode parent;
	ArrayList<TreeNode> children;
	
	private final double EXPLORATION_FACTOR = Math.sqrt(2);

	public TreeNode(gameBoard state) {
		this(state, null, null);
	}

	public TreeNode(gameBoard state, Move action, TreeNode parent) {
		this.state = state;
		this.action = action;
		this.parent = parent;
		children = new ArrayList<TreeNode>();
	}

	public TreeNode expand() {
		// Get list of possible actions
		ArrayList<Move> actions = Move.getActions(state);

		// Node is fully expanded or cannot be expanded
		if (actions.size() == children.size()) {
			return null;
		}

		// Make new state node for each action
		for (int i = 0; i < actions.size(); i++) {
			Move childAction = actions.get(i);

			gameBoard childState = state.copy();
			childState.playerTypeLocal = state.playerTypeLocal == 1 ? 2 : 1;
			childState.updateBoard(childAction.qCurr, childAction.qNew, childAction.arrow);

			// Add each node as a child of this node
			children.add(new TreeNode(childState, childAction, this));
		}

		// Return a random child
		int randIndex = (int) (ThreadLocalRandom.current().nextDouble() * children.size());
		return children.get(randIndex);
	}

	public int getWins() {
		return wins;
	}

	public int getVisits() {
		return visits;
	}

	public Move getAction() {
		return action;
	}

	double getUCB() {
		// EXPLORATION_FACTOR = constant defined at the top of the class.
		// If we hit 0, then the unvisited node should return infinity.
		if (this.getVisits() == 0) {
			return Double.MAX_VALUE;
		}

		// uct = v = total score / number of visits == avg value of the state.
		float uct = wins / visits;

		// apply the UCB1 function for that state
		if (parent != null) {
			uct += EXPLORATION_FACTOR * Math.sqrt(Math.log(parent.visits) / visits);
		}
		// Return ucb1 score.
		return uct;
	}
}
