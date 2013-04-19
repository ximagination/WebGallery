package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 5:12 PM
 */
public class IncorrectPrimaryKeyException extends ValidateException {

    public IncorrectPrimaryKeyException(String why) {
        super(why);
    }
}
