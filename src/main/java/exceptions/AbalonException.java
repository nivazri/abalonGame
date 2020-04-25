package exceptions;

/**
 * general abalon exception
 */
public class AbalonException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AbalonException() {
        super();
    }

    public AbalonException(String message) {
        super(message);
    }

    public AbalonException(String message, Throwable cause) {
        super(message, cause);
    }
}
