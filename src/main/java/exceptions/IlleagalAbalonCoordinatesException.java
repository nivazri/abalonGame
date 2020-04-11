package exceptions;

/**
 * an exception of illegal coordinates
 * @author omer
 *
 */
public class IlleagalAbalonCoordinatesException extends AbalonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	public IlleagalAbalonCoordinatesException()
	{
		super();
	}
	
	public IlleagalAbalonCoordinatesException(String message)
	{
		super(message);
	}
	public IlleagalAbalonCoordinatesException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
