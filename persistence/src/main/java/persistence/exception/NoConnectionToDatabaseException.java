package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 6:06 PM
 */
public class NoConnectionToDatabaseException extends D2DatabasePersistenceException {

    public NoConnectionToDatabaseException(String why) {
        super(why);
    }
}
