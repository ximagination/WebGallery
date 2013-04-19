package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 6:08 PM
 */
public class DriverClassNotFoundException extends PersistenceException {

    public DriverClassNotFoundException(String message) {
        super(message);
    }

    public DriverClassNotFoundException(Throwable cause) {
        super(cause);
    }

    public DriverClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
