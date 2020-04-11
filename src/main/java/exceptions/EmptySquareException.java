package exceptions;

/**
 * an exception of an empty square input
 * @author omer
 *
 */
public class EmptySquareException extends AbalonException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public EmptySquareException()
	{
		super();
	}
	public EmptySquareException(String message)
	{
		super(message);
	}
	public EmptySquareException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
