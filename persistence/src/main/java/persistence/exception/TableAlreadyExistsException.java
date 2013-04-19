package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 2:39 PM
 */
public class TableAlreadyExistsException extends PersistenceException {

    public TableAlreadyExistsException(String message) {
        super(message);
    }

    public TableAlreadyExistsException(String message, Throwable error) {
        super(message, error);
    }

    public TableAlreadyExistsException(Throwable error) {
        super(error);
    }
}
