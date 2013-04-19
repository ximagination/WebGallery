package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 3:49 PM
 */
public class EmptyFieldException extends ValidateException {

    public EmptyFieldException(String why) {
        super(why);
    }

}
