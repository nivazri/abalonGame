package exceptions;

/**
 * too many soldiers to move (more than 3)
 * @author omer
 *
 */
public class SoldierAmountException extends IllegalAbalonMoveException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	public SoldierAmountException()
	{
		super();
	}
	public SoldierAmountException(String message)
	{
		super(message);
	}
	public SoldierAmountException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
