package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 3:49 PM
 */
public class ToLongFieldException extends ValidationException {

    private int maxSizeOfField;

    public ToLongFieldException(String message, int maxSize) {
        super(message);
        this.maxSizeOfField = maxSize;
    }

    public ToLongFieldException(Throwable cause, int maxSize) {
        super(cause);
        this.maxSizeOfField = maxSize;
    }

    public ToLongFieldException(String message, Throwable cause, int maxSize) {
        super(message, cause);
        this.maxSizeOfField = maxSize;
    }

    public int getMaxSizeOfField() {
        return maxSizeOfField;
    }

}
