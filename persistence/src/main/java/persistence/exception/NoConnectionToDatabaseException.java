package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 6:06 PM
 */
public class NoConnectionToDatabaseException extends PersistenceException {

    public NoConnectionToDatabaseException(String why) {
        super(why);
    }

    public NoConnectionToDatabaseException(Throwable cause) {
        super(cause);
    }

    public NoConnectionToDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
