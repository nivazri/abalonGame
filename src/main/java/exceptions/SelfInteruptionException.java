package exceptions;

/**
 * a self interupting soldier
 * @author omer
 *
 */
public class SelfInteruptionException extends IllegalAbalonMoveException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5L;

	public SelfInteruptionException()
	{
		super();
	}
	public SelfInteruptionException(String message)
	{
		super(message);
	}
	
	public SelfInteruptionException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
