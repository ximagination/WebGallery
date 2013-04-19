package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 11:38 AM
 */
public class D2DatabasePersistenceException extends RuntimeException {

    public D2DatabasePersistenceException(String what) {
        super(what);
    }

    public D2DatabasePersistenceException(Throwable e) {
        super(e);
    }

}
