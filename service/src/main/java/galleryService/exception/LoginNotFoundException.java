package galleryService.exception;

import persistence.exception.ValidationException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/22/13
 * Time: 11:08 AM
 */
public class LoginNotFoundException extends ValidationException {

    public LoginNotFoundException(String message) {
        super(message);
    }

    public LoginNotFoundException(Throwable cause) {
        super(cause);
    }

    public LoginNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
