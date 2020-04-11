package boardGame;

import java.awt.Point;

import enums.Player;

/**
 * 
 * @author omer an abstract class that represents the data structure the board
 *         uses in order to receive and edit data
 * @param <T>
 */
public abstract class BoardDataStructure<T> {
	protected T _dataSet;

	public abstract Player getSquareContent(Point pos);

	public abstract void setSquareContent(Point pos, Player content);

	public abstract void initBoard();

	public T get_dataSet() {
		return _dataSet;
	}

	public void set_dataSet(T _dataSet) {
		this._dataSet = _dataSet;
	}

}
