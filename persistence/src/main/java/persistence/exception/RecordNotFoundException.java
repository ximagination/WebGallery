package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 5:10 PM
 */
public class RecordNotFoundException extends ValidationException {

    public RecordNotFoundException(String what) {
        super(what);
    }

    public RecordNotFoundException(Throwable error) {
        super(error);
    }

    public RecordNotFoundException(String message, Throwable error) {
        super(message, error);
    }
}

