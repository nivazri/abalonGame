package exceptions;

/**
 * a general illegal abalon move
 */
public class IllegalAbalonMoveException extends AbalonException {

    /**
     *
     */
    private static final long serialVersionUID = 8L;

    public IllegalAbalonMoveException() {
        super();
    }

    public IllegalAbalonMoveException(String message) {
        super(message);
    }

    public IllegalAbalonMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
