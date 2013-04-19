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

    public LoginAlreadyExistsException(Throwable error) {
        super(error);
    }

    public LoginAlreadyExistsException(String message, Throwable error) {
        super(message, error);
    }

}
