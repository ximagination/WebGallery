package galleryService.exception;

import persistence.exception.ValidationException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/22/13
 * Time: 11:11 AM
 */
public class IncorrectPasswordException extends ValidationException {

    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException(Throwable cause) {
        super(cause);
    }

    public IncorrectPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

}
