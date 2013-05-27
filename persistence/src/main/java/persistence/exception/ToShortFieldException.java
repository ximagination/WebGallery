package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 3:42 PM
 */
public class ToShortFieldException extends ValidationException {

    private int minSize;

    public ToShortFieldException(String message, int minSize) {
        super(message);
        this.minSize = minSize;
    }

    public ToShortFieldException(Throwable cause, int minSize) {
        super(cause);
        this.minSize = minSize;
    }

    public ToShortFieldException(String message, Throwable cause, int minSize) {
        super(message, cause);
        this.minSize = minSize;
    }

    public int getMinSizeOfField() {
        return minSize;
    }

}
