package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 3:49 PM
 */
public class EmptyFieldException extends ValidationException {

    public EmptyFieldException(String message) {
        super(message);
    }

    public EmptyFieldException(Throwable error) {
        super(error);
    }

    public EmptyFieldException(String message, Throwable error) {
        super(message, error);
    }

}
