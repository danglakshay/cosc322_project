package ubc.cosc322;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class monteCarlo {

	protected gameBoard board;

	private final long runtime = 5000;
	private final int threadCts = 4;
	private int numMoves;

	private TreeNode root;
	
	public monteCarlo(gameBoard board, int numMoves) {
		this.board = board;
		this.numMoves = numMoves;
	}

	protected Move move() {
		if(numMoves<15) {
			ArrayList<Move> actions = Move.getActions(board);
			Random random = new Random();
		    int randIdx = random.nextInt(actions.size());
		    Move randomMove = actions.get(randIdx);
		    return randomMove;
		}
		root = new TreeNode(board);

		long endTime = System.currentTimeMillis() + runtime;
		int iterations = 0;

		root.expand();
		ArrayList<TreeNode> nodeChildren = root.children;

		Thread[] threads = new Thread[threadCts];
		mctsRunnable[] runnables = new mctsRunnable[threadCts];

		//Splits searching across threads
		int numThreadChildren = nodeChildren.size() / threadCts;
		int numExtraYouth = nodeChildren.size() % threadCts;
		for (int threadIdx = 0; threadIdx < threadCts; threadIdx++) {
			TreeNode threadRoot = new TreeNode(root.state);

			//Each threads root gets section of children
			int index_s = numThreadChildren * threadIdx;
			int index_end = numThreadChildren * (threadIdx + 1);
			threadRoot.children = new ArrayList<>(nodeChildren.subList(index_s, index_end));
			System.out.println("From " + index_s + " to " + index_end);

			//First thread gets any extra children
			if (threadIdx == 0) {
				index_s = nodeChildren.size() - numExtraYouth;
				index_end = nodeChildren.size();
				threadRoot.children.addAll(new ArrayList<>(nodeChildren.subList(index_s, index_end)));
				System.out.println("EXTRA: From " + index_s + " to " + index_end);
			}

			mctsRunnable threadRunnable = new mctsRunnable(threadRoot, endTime);
			runnables[threadIdx] = threadRunnable;

			Thread thread = new Thread(threadRunnable);
			threads[threadIdx] = thread;
			thread.start();
		}

		//Syncs all threads
		for (int i = 0; i < threadCts; i++) {
			try {
				threads[i].join();
				iterations += runnables[i].iterations;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		root = bestMove(root);
		Move action = root.getAction();
		
		return action; 
	}
	
	private TreeNode bestMove(TreeNode root) {
		int maxWins = -1;
		TreeNode best = null;

		//Calculates opponents losses to compare nodes since wins represents next player playing for a given state
		for (TreeNode node : root.children) {
			int wins = node.getVisits() - node.getWins();
			if (wins > maxWins) {
				maxWins = wins;
				best = node;
			}
		}
		return best;
	}
	
	private class mctsRunnable implements Runnable {
		TreeNode root;
		long endTime;
		public int iterations;

		public mctsRunnable(TreeNode root, long endTime) {
			this.root = root;
			this.endTime = endTime;
			iterations = 0;
		}

		@Override
		public void run() {
			while (System.currentTimeMillis() < endTime) {
				TreeNode current = bestState(root);
				TreeNode child = current.expand();

				//Checks for terminal state
				if (child == null) {
					backpropagate(current,current.state.getOpponent());
					continue;
				}

				int winner = simulate(child);
				backpropagate(child, winner);

				iterations++;
			}
		}
	}
	
	private int simulate(TreeNode current) {
		gameBoard state = current.state.copy();
		int winner = -1;

		while (winner < 0) {
			ArrayList<Move> actions = Move.getActions(state);
			
			if (actions.size() == 0) {
				return state.getOpponent();
			}
			
			//Picks random move
			int moveIndex = (int) (ThreadLocalRandom.current().nextDouble() * actions.size());
			Move move = actions.get(moveIndex);

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
	
	private TreeNode bestState(TreeNode root) {
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
		ArrayList<Move> actions = Move.getActions(state);

		//Checks if node is fully expanded
		if (actions.size() == children.size()) {
			return null;
		}
		
		for (int i = 0; i < actions.size(); i++) {
			Move childAction = actions.get(i);
			gameBoard childState = state.copy();
			childState.playerTypeLocal = state.playerTypeLocal == 1 ? 2 : 1;
			childState.updateBoard(childAction.qCurr, childAction.qNew, childAction.arrow);
			children.add(new TreeNode(childState, childAction, this));
		}
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
		if (this.getVisits() == 0) {
			return Double.MAX_VALUE;
		}

		float uct = wins / visits;

		if (parent != null) {
			uct += EXPLORATION_FACTOR * Math.sqrt(Math.log(parent.visits) / visits);
		}
		
		return uct;
	}
}
