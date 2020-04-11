package abalonBoardGame;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import enums.Direction;
import enums.Player;
import exceptions.*;
import boardGame.Board;

/**
 * the class that holds the board of the abalon game and is in charge of knowing
 * the rules of the game and moving soldiers
 * 
 * @author omer
 *
 */
public class AbalonBoard extends
		Board<AbalonBoardDataStructure, AbalonSoldier[][]> {
	private final int _minNumberOfSoldiers = 7;
	private final int _maxNumberOfSoldiersToMove = 3;
	private final int _kill = 20;
	private final int _push = 10;
	private final int _isolatedSoldier = 50;
	private int _numOfRows;
	private int _numOfColsInFirstRow;

	/**
	 * constructor
	 * 
	 * @param numOfRows
	 *            - number of rows
	 * @param firstRowCols
	 *            - number of columns in first row
	 */
	public AbalonBoard(int numOfRows, int firstRowCols) {
		_numOfRows = numOfRows;
		_numOfColsInFirstRow = firstRowCols;
		_dataStructure = new AbalonBoardDataStructure(_numOfRows,
				_numOfColsInFirstRow);
	}
	
	/**
	 * constructor
	 * 
	 * @param ds
	 *            - custom board data structure
	 */
	public AbalonBoard(AbalonBoardDataStructure ds) {
		_numOfRows = ds.get_size();
		_numOfColsInFirstRow = ds.get_numOfColsInFirstRow();
		_dataStructure = ds;

		// Update the soldiers count in the board
		int colsInRow = _numOfColsInFirstRow;
		int rows = _numOfRows / 2;
		Point pos;
		
		// all the rows except the middle row
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < colsInRow; j++) {
				pos = new Point(j, i);
				if (ds.getSquareContent(pos) == Player.BLACK) {
					ds.set_numOfBlacks(ds.get_numOfBlacks() + 1);
				} else if (ds.getSquareContent(pos) == Player.WHITE) {
					ds.set_numOfWhites(ds.get_numOfWhites() + 1);
				}
				
				pos = new Point(j, _numOfRows - 1 - i);
				if (ds.getSquareContent(pos) == Player.BLACK) {
					ds.set_numOfBlacks(ds.get_numOfBlacks() + 1);
				} else if (ds.getSquareContent(pos) == Player.WHITE) {
					ds.set_numOfWhites(ds.get_numOfWhites() + 1);
				}
				
			}
			colsInRow++;
		}
		// middle row
		for (int i = 0; i < colsInRow; i++) {
			pos = new Point(i, _numOfRows / 2);
			
			if (ds.getSquareContent(pos) == Player.BLACK) {
				ds.set_numOfBlacks(ds.get_numOfBlacks() + 1);
			} else if (ds.getSquareContent(pos) == Player.WHITE) {
				ds.set_numOfWhites(ds.get_numOfWhites() + 1);
			}
		}
	}

	/**
	 * constructor that copies the board
	 * 
	 * @param board
	 *            - a board that will be copied
	 */
	public AbalonBoard(AbalonBoard board) {
		_numOfRows = board._numOfRows;
		_numOfColsInFirstRow = board._numOfColsInFirstRow;
		_dataStructure = new AbalonBoardDataStructure(board._dataStructure);
	}

	/**
	 * initilizes the first state of the board
	 */
	public void initBoard() {
		_dataStructure.initBoard();
	}

	/**
	 * sets the board contents according to a board
	 * 
	 * @param board
	 */
	public void setBoardContent(AbalonBoard board) {
		Point pos;
		int colsInRow = _numOfColsInFirstRow;
		int rows = _numOfRows / 2;
		// all the rows except the middle row
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < colsInRow; j++) {
				pos = new Point(j, i);
				_dataStructure.setSquareContent(pos, board.getDataStructure()
						.getSquareContent(pos));
				pos = new Point(j, _numOfRows - 1 - i);
				_dataStructure.setSquareContent(pos, board.getDataStructure()
						.getSquareContent(pos));
			}
			colsInRow++;
		}
		// middle row
		for (int i = 0; i < colsInRow; i++) {
			pos = new Point(i, _numOfRows / 2);
			_dataStructure.setSquareContent(pos, board.getDataStructure()
					.getSquareContent(pos));
		}
	}

	/**
	 * commits a move on the board assuming the input is matching to the rules
	 * of the game
	 */
	public void commitMove(Point source, Point dest) {
		Stack<Point> route = new Stack<Point>();
		Point tracker = new Point(source), p1, p2;
		Direction d = Direction
				.getDirectionFromPoints(source, dest, _numOfRows);
		Player player = _dataStructure.getSquareContent(source);
		Player opp;
		// go through the route until an empty slot or an exception from the
		// board bounds
		while (isPointInBoundsOfBoard(tracker)
				&& _dataStructure.getSquareContent(tracker) != null) {
			route.push(tracker);
			d.setMovementOffsetByCurrentLocation(tracker, _numOfRows);
			tracker = Direction.addOffsetToSource(tracker,
					d.getDirectionOffset());
		}
		// when a player kills an enemy soldier
		if (!isPointInBoundsOfBoard(tracker)) {
			// remove killed soldier from the board
			opp = player.getOpponent();
			_dataStructure.decNumOfSoldiersOfPlayer(opp);
			_dataStructure.setSquareContent(route.peek(), null);
		}
		// if the movement is without a kill
		else {
			route.push(tracker);
		}
		// move soldier/s forward
		while (!route.isEmpty()) {
			p1 = route.pop();
			if (!route.isEmpty()) {
				p2 = route.peek();
				_dataStructure.swapSquaresContents(p1, p2);
			}
		}
	}

	/**
	 * validates the move that is in the input returns true if legal throws an
	 * exception if not legal
	 */
	public boolean validateMove(Point source, Point dest, Player turn)
			throws AbalonException {
		// confirm that the player isn't trying to move an empty square
		if (_dataStructure.getSquareContent(source) == null) {
			throw new EmptySquareException("empty square");
		}
		// confirm that the coordinates are legal
		Vector<Point> upm = unconfirmedPossibleMoves(source);
		if (!upm.contains(dest)) {
			throw new IlleagalAbalonCoordinatesException("illegal coordinates");
		}
		// confirm that the move is legal
		Direction d = Direction
				.getDirectionFromPoints(source, dest, _numOfRows);
		Point tracker = new Point(source);
		Player player = _dataStructure.getSquareContent(tracker), opponent;
		// confirm that the player and the current turn match
		if (player != turn) {
			throw new IllegalPlayerTurnException("not the player's turn");
		}
		int numOfPlayer = 0;
		int numOfOpp = 0;
		// count player's soldiers
		while (isPointInBoundsOfBoard(tracker)
				&& _dataStructure.getSquareContent(tracker) == player) {
			numOfPlayer++;
			tracker = Direction.addOffsetToSource(tracker,
					d.getDirectionOffset());
			d.setMovementOffsetByCurrentLocation(tracker, _numOfRows);
		}
		// when a player tries to move more than three soldiers
		if (numOfPlayer > _maxNumberOfSoldiersToMove) {
			throw new SoldierAmountException("too many soldiers to move");
		}
		// if player tries to kill his own soldier
		if (!isPointInBoundsOfBoard(tracker)) {
			throw new IllegalAbalonMoveException("illeagal move");
		}
		// if tries to move soldier/s to an empty spot
		if (_dataStructure.getSquareContent(tracker) == null) {
			return true;
		}
		opponent = player.getOpponent();
		// count opponent's soldiers
		while (isPointInBoundsOfBoard(tracker)
				&& _dataStructure.getSquareContent(tracker) == opponent) {
			numOfOpp++;
			tracker = Direction.addOffsetToSource(tracker,
					d.getDirectionOffset());
			d.setMovementOffsetByCurrentLocation(tracker, _numOfRows);
		}
		// if a player's soldier is in the way
		if (isPointInBoundsOfBoard(tracker)
				&& _dataStructure.getSquareContent(tracker) == player) {
			throw new SelfInteruptionException("self interupting soldiers");
		}
		// if opponent matches the players force or greater
		if (numOfOpp >= numOfPlayer || numOfOpp >= _maxNumberOfSoldiersToMove) {
			throw new InsufficientNumberOfSoldiersException(
					"enemy overpowers you");
		}
		// if player kills enemy soldier
		if (!isPointInBoundsOfBoard(tracker)) {
			return true;
		}
		// if player tries to push enemy soldier/s
		return true;
	}

	/**
	 * returns all the possible coordinates that a soldier can move to
	 * 
	 * @param source
	 *            - the coordinate of the soldier
	 * @return a vector that has all the possible moves according to the board
	 *         is public for graphical use
	 */
	public Vector<Point> unconfirmedPossibleMoves(Point source) {
		Point dest;
		Vector<Point> vec = new Vector<Point>();
		Direction.setDirectionsOffsetsByCurrPos(source, _numOfRows);
		for (Direction d : Direction.values()) {
			dest = Direction.addOffsetToSource(source, d.getDirectionOffset());
			// if the coordinate is in the bounds of the board
			if (isPointInBoundsOfBoard(dest)) {
				vec.add(dest);
			}
		}
		return vec;
	}

	/**
	 * 
	 * @param pos
	 *            - a point
	 * @return true if the pos is in the bounds of the board else returns false
	 */
	private boolean isPointInBoundsOfBoard(Point pos) {
		return pos.y > -1 && pos.y < _numOfRows && pos.x > -1
				&& pos.x < numOfColsInRow(pos.y);
	}

	/**
	 * 
	 * @param rowNum
	 *            - a row number
	 * @return the number of columns in the row
	 */
	private int numOfColsInRow(int rowNum) {
		if (rowNum < _numOfRows / 2) {
			return rowNum + 5;
		}
		return -(rowNum - (_numOfRows - 1)) + 5;
	}

	/**
	 * returns the winner of the current state
	 */
	public Player getWinner() {
		if (_dataStructure.get_numOfBlacks() < _minNumberOfSoldiers)
			return Player.WHITE;
		if (_dataStructure.get_numOfWhites() < _minNumberOfSoldiers)
			return Player.BLACK;
		return null;
	}

	/**
	 * evaluates the current state according to the input of the player and
	 * returns it's value
	 */
	public int evaluate(Player p) {
		int calc = 0;
		/**
		 * soldiers that can push opponent soldiers that can be pushed by
		 * opponent soldiers that can kill opponent soldiers that can be killed
		 * especially high rank to a state in which the last kill is achieved
		 * (winning state) (maybe) add a calculation of all the single soldiers
		 * out there and reduce the value of the state (maybe) add a calculation
		 * of (number of player soldiers - number of enemy soldiers)
		 */

		int colsInRow = _numOfColsInFirstRow;
		int rows = _numOfRows / 2;
		Point pos;
		Player opp = p.getOpponent();
		// if a winning state
		if (getWinner() == p) {
			return (int) Math.pow(10, 8);
		}
		// if a losing state
		if (getWinner() == opp) {
			return -((int) Math.pow(10, 8));
		}

		// scan the board and evaluate each friendly and hostile soldier
		// all the rows except the middle row
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < colsInRow; j++) {
				pos = new Point(j, i);
				calc += evaluateSinglePosition(pos, p);
				pos = new Point(j, _numOfRows - 1 - i);
				calc += evaluateSinglePosition(pos, p);
			}
			colsInRow++;
		}
		// middle row
		for (int i = 0; i < colsInRow; i++) {
			pos = new Point(i, _numOfRows / 2);
			calc += evaluateSinglePosition(pos, p);
		}

		// consider the difference between the player's soldier amount minus the
		// opponent's soldier amount
		calc += (_dataStructure.numOfSoldiers(p) - _dataStructure
				.numOfSoldiers(opp)) * 100;
		return calc;
	}

	/**
	 * evaluates a single position in the board
	 * 
	 * @param pos
	 *            - the position in the board
	 * @param p
	 *            - the player that is being evaluated
	 * @return the value of the position
	 */
	private int evaluateSinglePosition(Point pos, Player p) {
		int calc = 0;
		Vector<Point> threats;
		Player opp = p.getOpponent();
		// if the square contains a player's soldier
		if (_dataStructure.getSquareContent(pos) == p) {
			// add a calculation that it is better for the soldiers to be close
			// to the center of the board
			calc += numOfColsInRow(pos.y);
			threats = threateningEnemySoldiers(pos);
			for (Point threat : threats) {
				// if the threat can kill a player's soldier
				if (isPotentialDeathOfSoldier(pos, threat)) {
					calc -= _kill;
					// if the player is about to lose
					if (isInRiskOfLoss(p)) {
						calc -= Math.pow(10, 5);
					}
				} else {
					calc -= _push;
				}
			}
			// if a friendly soldier is isolated
			if (isAloneSoldier(pos)) {
				calc -= _isolatedSoldier;
			}
			return calc;
		}
		// if the square contains an opponent's soldier
		if (_dataStructure.getSquareContent(pos) == opp) {
			threats = threateningEnemySoldiers(pos);
			for (Point threat : threats) {
				// if the threat can kill a player's soldier
				if (isPotentialDeathOfSoldier(pos, threat)) {
					calc += _kill;
					// if the player can win after a kill
					if (isInRiskOfLoss(opp)) {
						calc += Math.pow(10, 5);
					}
				} else {
					calc += _push;
				}
			}
			// if an enemy has been isolated
			if (isAloneSoldier(pos)) {
				calc += _isolatedSoldier * 1000;
			}
			return calc;
		}
		// if the square is empty
		return 0;
	}

	/**
	 * 
	 * @param p
	 *            - player
	 * @return true if the player is in danger of losing the game, else returns
	 *         false
	 */
	private boolean isInRiskOfLoss(Player p) {
		if (_dataStructure.numOfSoldiers(p) == _minNumberOfSoldiers) {
			return true;
		}
		return false;
	}

	/**
	 * returns the enemies around a soldier that are threatning to push or kill
	 * him
	 * 
	 * @param soldier
	 */
	private Vector<Point> threateningEnemySoldiers(Point soldier) {
		Vector<Point> threats = new Vector<Point>();
		Vector<Point> enemies = surroundingEnemies(soldier);
		Player player = _dataStructure.getSquareContent(soldier);
		Player opp = player.getOpponent();
		Point pos;
		Point source;
		Point dest;
		Stack<Point> route;
		int counter;
		Direction d;
		for (Point enemy : enemies) {
			Direction.setDirectionsOffsetsByCurrPos(soldier, _numOfRows);
			d = Direction.getDirectionFromPoints(soldier, enemy, _numOfRows);

			route = new Stack<Point>();
			pos = new Point(soldier);
			route.push(pos);
			d.setMovementOffsetByCurrentLocation(pos, _numOfRows);
			pos = Direction.addOffsetToSource(pos, d.getDirectionOffset());
			counter = 1;
			while (isPointInBoundsOfBoard(pos)
					&& _dataStructure.getSquareContent(pos) == opp
					&& counter <= _maxNumberOfSoldiersToMove) {
				route.push(pos);
				d.setMovementOffsetByCurrentLocation(pos, _numOfRows);
				pos = Direction.addOffsetToSource(pos, d.getDirectionOffset());
				counter++;
			}
			source = route.pop();
			if (!route.isEmpty()) {
				dest = route.pop();
			} else {
				dest = soldier;
			}
			try {
				validateMove(source, dest, opp);
				threats.add(enemy);
			} catch (AbalonException e) {
				// add the potential threat only if he can push the soldier
			}

		}
		return threats;
	}

	/**
	 * 
	 * @param soldier
	 * @return all the enemies that surround a soldier
	 */
	private Vector<Point> surroundingEnemies(Point soldier) {
		Vector<Point> enemies = new Vector<Point>();
		Player player = _dataStructure.getSquareContent(soldier);
		Player opp = player.getOpponent();
		Vector<Point> surroundingSquares = unconfirmedPossibleMoves(soldier);
		for (Point pos : surroundingSquares) {
			if (_dataStructure.getSquareContent(pos) == opp) {
				enemies.add(pos);
			}
		}
		return enemies;
	}

	/**
	 * 
	 * @param soldier
	 *            - in danger of death
	 * @param opponent
	 *            - the executioner
	 * @return true if a soldier of the player can die by the push of the
	 *         opponent
	 */
	private boolean isPotentialDeathOfSoldier(Point soldier, Point opponent) {

		int counter = 0, numOfPlayers;
		Stack<Point> route = new Stack<Point>();
		AbalonBoard temp;
		Player player = _dataStructure.getSquareContent(soldier);
		Player opp = _dataStructure.getSquareContent(opponent);

		Direction.setDirectionsOffsetsByCurrPos(soldier, _numOfRows);

		Direction d = Direction.getDirectionFromPoints(soldier, opponent,
				_numOfRows);
		Point tracker = soldier;
		Point source = opponent;
		Point dest = soldier;
		tracker = Direction.addOffsetToSource(tracker, d.getDirectionOffset());
		counter++;
		while (isPointInBoundsOfBoard(tracker)
				&& _dataStructure.getSquareContent(tracker) == opp
				&& counter <= _maxNumberOfSoldiersToMove) {
			route.push(tracker);
			d.setMovementOffsetByCurrentLocation(tracker, _numOfRows);
			tracker = Direction.addOffsetToSource(tracker,
					d.getDirectionOffset());
			counter++;
		}
		source = route.pop();
		// can assume stack is not empty because this function gets coordinates
		// of pushes only
		if (!route.isEmpty()) {
			dest = route.pop();
		} else {
			dest = soldier;
		}

		temp = new AbalonBoard(this);
		numOfPlayers = temp._dataStructure.numOfSoldiers(player);
		temp.commitMove(source, dest);
		// if a soldier of the player can be killed by a shove
		if (numOfPlayers > temp._dataStructure.numOfSoldiers(player)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param soldier
	 * @return true if the soldier is not around any friendlies
	 */
	private boolean isAloneSoldier(Point soldier) {
		Player player = _dataStructure.getSquareContent(soldier);
		Vector<Point> upm = unconfirmedPossibleMoves(soldier);
		for (Point pos : upm) {
			// if the soldier has next to him a friendly soldier
			if (_dataStructure.getSquareContent(pos) == player) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns the next states of the board of a player
	 */
	public Collection<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> getNextStates(
			Player p) {
		Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> nextStates = new Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>>();
		Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> temp;
		int colsInRow = _numOfColsInFirstRow;
		int rows = _numOfRows / 2;
		Point pos;
		// all the rows except the middle row
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < colsInRow; j++) {
				pos = new Point(j, i);
				if (_dataStructure.getSquareContent(pos) == p) {
					temp = (Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>>) getNextStatesOfSoldier(
							pos, p);
					nextStates.addAll(temp);
				}
				pos = new Point(j, _numOfRows - 1 - i);
				if (_dataStructure.getSquareContent(pos) == p) {
					temp = (Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>>) getNextStatesOfSoldier(
							pos, p);
					nextStates.addAll(temp);
				}
			}
			colsInRow++;
		}
		// middle row
		for (int i = 0; i < colsInRow; i++) {
			pos = new Point(i, _numOfRows / 2);
			if (_dataStructure.getSquareContent(pos) == p) {
				temp = (Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>>) getNextStatesOfSoldier(
						pos, p);
				nextStates.addAll(temp);
			}
		}
		return nextStates;
	}

	/**
	 * 
	 * @param pos
	 *            - position of the soldier
	 * @param p
	 *            - current turn
	 * @return the next states of a soldier in the board
	 */
	private Collection<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> getNextStatesOfSoldier(
			Point pos, Player p) {
		Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> nextStatesOfPlayer = new Vector<Board<AbalonBoardDataStructure, AbalonSoldier[][]>>();
		AbalonBoard boardTemp;
		Direction.setDirectionsOffsetsByCurrPos(pos, _numOfRows);
		Point dest;
		for (Direction d : Direction.values()) {
			boardTemp = new AbalonBoard(this);
			dest = d.getDestPoint(pos);
			try {
				boardTemp.validateMove(pos, dest, p);
				boardTemp.commitMove(pos, dest);
				nextStatesOfPlayer.add(boardTemp);
			} catch (AbalonException e) {
				// if the move is legal than add the state to the vector, else
				// don't do anything
			}
		}
		return nextStatesOfPlayer;
	}

	/**
	 * 
	 * @return a description of the board
	 */
	public Player[][] boardDescription() {
		return _dataStructure.boardDescription();
	}

	/**
	 * returns a string that describes the board
	 */
	public String toString() {
		return _dataStructure.toString();
	}

}
