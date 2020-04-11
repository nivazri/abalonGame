package boardGame;

import java.awt.Point;
import java.util.Collection;

import enums.*;

/**
 * 
 * @author omer an abstract class that represents the board and is in charge of
 *         moving the soldier and to validate moves
 * @param <T>- the data structure
 * @param <E>- the object that stores the data
 */
public abstract class Board<T extends BoardDataStructure<E>, E> {
	protected T _dataStructure;

	public abstract void initBoard();

	public abstract Collection<Board<T, E>> getNextStates(Player p);

	public abstract void commitMove(Point source, Point dest);

	public abstract boolean validateMove(Point source, Point dest, Player turn)
			throws Exception;

	public abstract Player getWinner();

	public abstract int evaluate(Player p);

	public T getDataStructure() {
		return _dataStructure;
	}

	public void setDataStructure(T _dataStructure) {
		this._dataStructure = _dataStructure;
	}
}
