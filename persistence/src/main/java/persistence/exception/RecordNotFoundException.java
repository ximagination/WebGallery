package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 5:10 PM
 */
public class RecordNotFoundException extends ValidateException {

    public RecordNotFoundException(String what) {
        super(what);
    }
}

