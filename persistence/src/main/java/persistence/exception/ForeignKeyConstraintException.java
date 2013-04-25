package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 3:12 PM
 */
public class ForeignKeyConstraintException extends ValidationException {

    public ForeignKeyConstraintException(String message) {
        super(message);
    }

    public ForeignKeyConstraintException(Throwable cause) {
        super(cause);
    }

    public ForeignKeyConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

}
