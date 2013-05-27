package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 4:24 PM
 */
public class IndexNotCreatedException extends PersistenceException {

    public IndexNotCreatedException(String message) {
        super(message);
    }

    public IndexNotCreatedException(Throwable cause) {
        super(cause);
    }

    public IndexNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

}
