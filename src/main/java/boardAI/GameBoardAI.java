package boardAI;

import java.util.Collection;
import java.util.Vector;

import boardGame.Board;
import enums.Player;

public class GameBoardAI<T extends Board<?, ?>> implements BoardSolver<T> {

	private int _level;

	/**
	 * default constructor
	 */
	public GameBoardAI() {

	}

	/**
	 * the function gets the currennt next states of a player in the board and
	 * returns the best state for the player
	 */
	public T findBestMove(Collection<T> currentStates, Player currentTurn) {
		T selectedState;
		// initialize the root of the tree
		MinMaxTreeNode<T> minMaxTree = new MinMaxTreeNode<T>(null,
				statesToTreeNodes(currentStates));
		constructMinMaxTree(minMaxTree, _level, currentTurn, currentTurn);
		selectedState = minMaxAlgorithm(minMaxTree);
		return selectedState;
	}

	/**
	 * the minimax algorithm returns the best state for the player
	 * @param root-
	 * @return
	 */
	private T minMaxAlgorithm(MinMaxTreeNode<T> root) {
		// set the minMax node values by the values of the leaf nodes
		minMaxAlgorithmWithAlphaBetaPruning(root, true, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
		// return the state with the biggest value
		return stateWithBiggestValue(root.getSons());
	}

	/**
	 * 
	 * @param sons- a vector of tree nodes
	 * @return the node with the biggest value
	 */
	private T stateWithBiggestValue(Vector<MinMaxTreeNode<T>> sons) {
		MinMaxTreeNode<T> maxNode = sons.get(0);
		for (MinMaxTreeNode<T> treeNode : sons) {
			if (treeNode.getValue() > maxNode.getValue()) {
				maxNode = treeNode;
			}
		}
		return maxNode.getState();
	}

	
	/**
	 * 
	 * @param node- current node
	 * @param isMaxNode- true if max node false if min node
	 * @param alpha- best option for the computer along the path to the root
	 * @param beta-  best option for the player along the path to the root
	 * @return the value of the node
	 */
	private int minMaxAlgorithmWithAlphaBetaPruning(MinMaxTreeNode<T> node,
			boolean isMaxNode, int alpha, int beta) {
		// maximizer- computer
		// alpha- best option for the maximizer along the path to the root
		// minimizer- player
		// beta- best option for the minimizer along the path to the root
		int value;
		// if a leaf node
		if (node.getSons().isEmpty()) {
			return node.getValue();
		}
		// if maximum node
		if (isMaxNode) {
			for (MinMaxTreeNode<T> son : node.getSons()) {
				value = minMaxAlgorithmWithAlphaBetaPruning(son, false, alpha,
						beta);
				// if the son's value is bigger than the father's value in max
				// node
				if (value > node.getValue()) {
					node.setValue(value);
				}
				// if a better option was found for the max node
				if (value > alpha) {
					alpha = value;
				}
				// if the maximizer found a node that will never be chosen by
				// the minimizer
				if (alpha >= beta) {
					// prune
					node.getSons().remove(son);
					break;
				}
			}
			return alpha;
		}
		// if minimum node
		for (MinMaxTreeNode<T> son : node.getSons()) {
			value = minMaxAlgorithmWithAlphaBetaPruning(son, true, alpha, beta);
			// if the son's value is smaller than the father's value in min node
			if (value < node.getValue()) {
				node.setValue(value);
			}
			// if a better option was found for the min node
			if (beta > value) {
				beta = value;
			}
			// if the minimizer found a node that will never be chosen by the
			// maximizer
			if (alpha >= beta) {
				// prune
				node.getSons().remove(son);
				break;
			}
		}
		return beta;
	}

	/**
	 * 
	 * @param node
	 * @param depth
	 * @param turn
	 *            - by which player to determine the next states
	 * @param player
	 *            - by which player to evaluate the leaf states
	 */
	private void constructMinMaxTree(MinMaxTreeNode<T> node, int depth,
			Player turn, Player player) {
		if (depth > 0) {
			// if max node
			if (player == turn) {
				node.setValue(Integer.MIN_VALUE);
			}
			// if min node
			else {
				node.setValue(Integer.MAX_VALUE);
			}
			// build each node it's sons except the root that already has the
			// sons
			if (node.getState() != null) {
				buildSonsToTreeNode(node, turn);
			}
			for (MinMaxTreeNode<T> son : node.getSons()) {
				constructMinMaxTree(son, depth - 1, turn.getOpponent(), player);
			}
		}

		// if leaf node
		else {
			// the leaf node will evaluate
			node.setValue(node.getState().evaluate(player));
		}
	}
	
	/**
	 * 
	 * @param states
	 * @return the states in a vector after convertion to tree nodes
	 */
	private Vector<MinMaxTreeNode<T>> statesToTreeNodes(Collection<T> states) {
		Vector<MinMaxTreeNode<T>> sons = new Vector<MinMaxTreeNode<T>>();
		MinMaxTreeNode<T> son;
		for (T state : states) {
			son = new MinMaxTreeNode<T>(state);
			sons.add(son);
		}
		return sons;
	}

	/**
	 * builds the sons of a current tree node
	 * @param node
	 * @param p
	 */
	private void buildSonsToTreeNode(MinMaxTreeNode<T> node, Player p) {
		Collection<T> states = (Collection<T>) node.getState().getNextStates(p);
		Vector<MinMaxTreeNode<T>> sons = statesToTreeNodes(states);
		node.setSons(sons);
	}


	/* getters and setters */
	public int getLevel() {
		return _level;
	}

	public void setLevel(int _level) {
		this._level = _level;
	}

	/********************/
	// function that assist in analyzing the tree, they have nothing to do with
	// the algorithm

	private int numOfNodes(MinMaxTreeNode<T> node) {
		int count = 0;
		if (node == null) {
			return 0;
		}
		if (node.getSons().isEmpty()) {
			return 1;
		}
		for (MinMaxTreeNode<T> son : node.getSons()) {
			count += numOfNodes(son);
		}
		return 1 + count;
	}

	private void printLeavesValues(MinMaxTreeNode<T> node) {
		if (node.getSons().isEmpty()) {
			System.out.println(node.getValue());
		} else {
			for (MinMaxTreeNode<T> son : node.getSons()) {
				printLeavesValues(son);
			}
		}
	}

	private void showRoute(MinMaxTreeNode<T> root) {
		showRoute(root, true);
	}

	private void showRoute(MinMaxTreeNode<T> root, boolean isMaxNode) {
		if (root != null) {
			if (isMaxNode) {
				if (root.getState() != null) {
					System.out.println(root.getState().toString());
				}
				System.out.println("node value:" + root.getValue());
				showRoute(maxNode(root.getSons()), false);
			} else {
				if (root.getState() != null) {
					System.out.println(root.getState().toString());
				}
				System.out.println("node value:" + root.getValue());
				showRoute(minNode(root.getSons()), true);
			}
		}
	}

	private MinMaxTreeNode<T> maxNode(Vector<MinMaxTreeNode<T>> sons) {
		if (sons.isEmpty())
			return null;
		MinMaxTreeNode<T> maxNode = sons.get(0);
		for (MinMaxTreeNode<T> treeNode : sons) {
			if (treeNode.getValue() > maxNode.getValue()) {
				maxNode = treeNode;
			}
		}
		return maxNode;
	}

	private MinMaxTreeNode<T> minNode(Vector<MinMaxTreeNode<T>> sons) {
		if (sons.isEmpty())
			return null;
		MinMaxTreeNode<T> minNode = sons.get(0);
		for (MinMaxTreeNode<T> treeNode : sons) {
			if (treeNode.getValue() < minNode.getValue()) {
				minNode = treeNode;
			}
		}
		return minNode;
	}


	/**
	 * 
	 * @param node
	 * @param isMaxNode
	 */
	private void minMaxAlgorithm(MinMaxTreeNode<T> node, boolean isMaxNode) {
		// if not a leaf node
		if (!node.getSons().isEmpty()) {
			// if max node
			if (isMaxNode) {
				for (MinMaxTreeNode<T> son : node.getSons()) {
					minMaxAlgorithm(son, false);
					// if the son's value is bigger than the father's value in
					// max node
					if (son.getValue() > node.getValue()) {
						node.setValue(son.getValue());
					}
				}
			}
			// if min node
			else {
				for (MinMaxTreeNode<T> son : node.getSons()) {
					minMaxAlgorithm(son, true);
					// if the son's value is smaller than the father's value in
					// min node
					if (son.getValue() < node.getValue()) {
						node.setValue(son.getValue());
					}
				}
			}
		}
	}
}

/*
 * System.out.println("num of nodes before prune:"+numOfNodes(minMaxTree));
 * System.out.println("next states before algorithm:"); for (T t :
 * currentStates) { System.out.print(t.evaluate(currentTurn)+" "); }
 * System.out.println(); selectedState=minMaxAlgorithm(minMaxTree);
 * System.out.println("next states values:"); for (MinMaxTreeNode<T> node :
 * minMaxTree.getSons()) { System.out.print(node.getValue()+" "); }
 * System.out.println();
 * System.out.println("num of nodes after prune:"+numOfNodes(minMaxTree));
 * 
 * System.out.println("leaves values:"); //printLeavesValues(minMaxTree);
 * System.out.println();
 * 
 * System.out.println("minmax tree route:"); showRoute(minMaxTree);
 */

