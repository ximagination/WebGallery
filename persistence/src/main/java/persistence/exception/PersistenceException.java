package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 11:38 AM
 */
public class PersistenceException extends RuntimeException {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(Throwable error) {
        super(error);
    }

    public PersistenceException(String message, Throwable error) {
        super(message, error);
    }

}
