package exceptions;

/**
 * an insufficient a number of soldiers to move the enemy
 * @author omer
 *
 */
public class InsufficientNumberOfSoldiersException extends IllegalAbalonMoveException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;
	public InsufficientNumberOfSoldiersException()
	{
		super();
	}
	public InsufficientNumberOfSoldiersException(String message)
	{
		super(message);
	}
	public InsufficientNumberOfSoldiersException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
