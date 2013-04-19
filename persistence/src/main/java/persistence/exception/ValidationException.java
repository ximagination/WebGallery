package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 3:46 PM
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
