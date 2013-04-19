package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 4:02 PM
 */
public class LoginAlreadyExistsException extends ValidateException {

    public LoginAlreadyExistsException(String what) {
        super(what);
    }
}
