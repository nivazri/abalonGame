package abalonBoardGame;

import java.awt.Point;
import enums.*;
import boardGame.*;

import exceptions.AbalonException;

/**
 * 
 * @author omer represents the abalon board game class, is in charge of managing
 *         the game
 */
public class AbalonBoardGame extends BoardGame<AbalonBoard> {

	/**
	 * constructor
	 * 
	 * @param numOfRows- number of rows in the board
	 * @param numOfColsInFirstRow- number of columns in the first row of the board
	 * necessary in order to build the hexagon
	 */
	public AbalonBoardGame(int numOfRows, int numOfColsInFirstRow) {
		_board = new AbalonBoard(numOfRows, numOfColsInFirstRow);
		_turnsContainer = new PlayerRepository();
		_iterator = _turnsContainer.getIterator();
		switchTurn();// Initialize first turn
	}

	/**
	 * returns the winner of the game
	 */
	public Player getWinner() {
		return _board.getWinner();
	}

	/**
	 * validates the input and moves the soldiers
	 */
	public void move(Point source, Point dest) {
		try {
			_board.validateMove(source, dest, _turn);
			_board.commitMove(source, dest);
			switchTurn();
		} catch (AbalonException e) {
			// in case the move was illegal
			System.out.println(e.getMessage());
		}
	}

	/**
	 * initializes the board
	 */
	public void initBoard() {
		_board.initBoard();
	}
	
	/**
	 * returns a matrix that describes the board for graphical purposes
	 * @return
	 */
	public Player[][] boardDescription() {
		return _board.boardDescription();
	}

	/**
	 * returns a string that describes the board
	 */
	public String toString() {
		return _board.toString();
	}
}
