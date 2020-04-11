package boardAI;

import java.util.Vector;

import boardGame.Board;

/**
 * 
 * @author omer
 *
 * @param <T> a state of the board
 */
public class MinMaxTreeNode<T extends Board<?,?>> {
	private T _state;
	private int _value;
	private Vector<MinMaxTreeNode<T>> _sons;
	
	/**
	 * constructor that builds a tree node with a given state
	 * @param item
	 */
	public MinMaxTreeNode(T item)
	{
		_state=item;
		_sons=new Vector<MinMaxTreeNode<T>>();
	}
	
	/**
	 * constructor that builds a tree node with a given state and sons
	 * @param item
	 * @param sons
	 */
	public MinMaxTreeNode(T item,Vector<MinMaxTreeNode<T>> sons)
	{
		_state=item;
		_sons=sons;
	}
	
	// getters and setters
	public T getState() {
		return _state;
	}

	public void setState(T _state) {
		this._state = _state;
	}

	public Vector<MinMaxTreeNode<T>> getSons() {
		return _sons;
	}

	public void setSons(Vector<MinMaxTreeNode<T>> _sons) {
		this._sons = _sons;
	}

	public int getValue() {
		return _value;
	}

	public void setValue(int _value) {
		this._value = _value;
	}
}
