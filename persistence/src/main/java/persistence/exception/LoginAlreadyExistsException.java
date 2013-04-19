package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 4:02 PM
 */
public class LoginAlreadyExistsException extends ValidationException {

    public LoginAlreadyExistsException(String message) {
        super(message);
    }

    public LoginAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public LoginAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
