package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 6:08 PM
 */
public class DriverClassNotFoundException extends D2DatabasePersistenceException {

    public DriverClassNotFoundException(String why) {
        super(why);
    }
}
