package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/20/13
 * Time: 12:24 PM
 */
public class IncorrectValueException extends ValidationException {

    public IncorrectValueException(String message) {
        super(message);
    }

    public IncorrectValueException(Throwable cause) {
        super(cause);
    }

    public IncorrectValueException(String message, Throwable cause) {
        super(message, cause);
    }

}
