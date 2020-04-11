package exceptions;

/**
 * a player turn mismatch
 * @author omer
 *
 */
public class IllegalPlayerTurnException extends AbalonException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7L;
	public IllegalPlayerTurnException()
	{
		super();
	}
	public IllegalPlayerTurnException(String message)
	{
		super(message);
	}
	public IllegalPlayerTurnException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
