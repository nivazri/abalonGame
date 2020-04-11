package boardGame;

import java.awt.Point;

import enums.*;

/**
 * 
 * @author omer an abstract class that every board game can inherit from
 *         represents a general board game manager that holds the board as a
 *         parameter
 * @param <T>- board
 */
public abstract class BoardGame<T extends Board<?, ?>> {
	protected T _board;
	protected PlayerContainer _turnsContainer;
	protected Player _turn;
	protected PlayerIterator _iterator;

	public abstract Player getWinner();

	public abstract void move(Point source, Point dest);

	public abstract void initBoard();

	/**
	 * a function that switches the turn to the next one
	 */
	public void switchTurn() {
		_turn = (Player) _iterator.next();
	}

	public T getBoard() {
		return _board;
	}

	public void setBoard(T _board) {
		this._board = _board;
	}

	public PlayerContainer get_turnsContainer() {
		return _turnsContainer;
	}

	public void setTurnsContainer(PlayerContainer _turnsContainer) {
		this._turnsContainer = _turnsContainer;
	}

	public Player getTurn() {
		return _turn;
	}

	public void setTurn(Player _turn) {
		this._turn = _turn;
	}

	public PlayerIterator getIterator() {
		return _iterator;
	}

	public void setIterator(PlayerIterator _iterator) {
		this._iterator = _iterator;
	}
}
