package persistence.exception;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 6:13 PM
 */
public class UnknownDataSourceException extends RuntimeException {

    public UnknownDataSourceException(String why) {
        super(why);
    }

}
