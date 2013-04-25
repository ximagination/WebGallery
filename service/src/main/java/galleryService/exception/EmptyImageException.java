package galleryService.exception;

import persistence.exception.ValidationException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 6:36 PM
 */
public class EmptyImageException extends ValidationException {

    public EmptyImageException(String message) {
        super(message);
    }

    public EmptyImageException(Throwable cause) {
        super(cause);
    }

    public EmptyImageException(String message, Throwable cause) {
        super(message, cause);
    }

}
