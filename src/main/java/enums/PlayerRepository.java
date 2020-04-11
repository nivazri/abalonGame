package enums;

/**
 * the class that holds the order of the turns
 * 
 * @author omer design pattern iterator
 */
public class PlayerRepository implements PlayerContainer {
	private Player[] _turns;

	/**
	 * constructor that initializes the turns
	 */
	public PlayerRepository() {
		_turns = Player.values();
	}

	/**
	 * turn generator
	 */
	public PlayerGenrator getIterator() {
		return new PlayerGenrator();
	}

	/**
	 * a private class that is in charge of the order of the turns
	 * 
	 * @author omer
	 *
	 */
	private class PlayerGenrator implements PlayerIterator {
		private int _index;

		/**
		 * constructor
		 */
		public PlayerGenrator() {
			_index = 0;
		}

		/**
		 * returns true if didn't reach the end of the turn
		 */
		public boolean hasNext() {
			return _index < _turns.length;
		}

		/**
		 * returns the next object in the repository
		 */
		public Object next() {
			if (!hasNext()) {
				_index = 0;
			}
			return _turns[_index++];
		}
	}
}
