package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 3:49 PM
 */
public class ToLongFieldException extends ValidateException {

    public final int maxSizeOfField;

    public ToLongFieldException(String why, int maxSize) {
        super(why);
        this.maxSizeOfField = maxSize;
    }
}
