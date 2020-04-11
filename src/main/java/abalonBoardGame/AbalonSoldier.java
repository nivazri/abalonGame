package abalonBoardGame;

import enums.Player;

/**
 * a class that holds information on a specific abalon soldier
 * 
 * @author omer
 *
 */
public class AbalonSoldier {
	private Player _type;

	/**
	 * constructor
	 * 
	 * @param p
	 */
	public AbalonSoldier(Player p) {
		_type = p;
	}

	/**
	 * constructor that creates a copy of a given abalon soldier
	 * 
	 * @param a
	 */
	public AbalonSoldier(AbalonSoldier a) {
		_type = a._type;
	}

	public Player getType() {
		return _type;
	}
}
