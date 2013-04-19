package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 3:49 PM
 */
public class ToLongFieldException extends ValidationException {

    public final int maxSizeOfField;

    public ToLongFieldException(String message, int maxSize) {
        super(message);
        this.maxSizeOfField = maxSize;
    }

    public ToLongFieldException(Throwable error, int maxSize) {
        super(error);
        this.maxSizeOfField = maxSize;
    }

    public ToLongFieldException(String message, Throwable error, int maxSize) {
        super(message, error);
        this.maxSizeOfField = maxSize;
    }

}
