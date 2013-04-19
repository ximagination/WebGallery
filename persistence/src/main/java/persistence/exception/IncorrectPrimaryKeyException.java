package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 5:12 PM
 */
public class IncorrectPrimaryKeyException extends ValidationException {

    public IncorrectPrimaryKeyException(String message) {
        super(message);
    }

    public IncorrectPrimaryKeyException(Throwable cause) {
        super(cause);
    }

    public IncorrectPrimaryKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
