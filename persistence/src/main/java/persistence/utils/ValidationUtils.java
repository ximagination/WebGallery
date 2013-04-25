package persistence.utils;

import persistence.exception.IncorrectPrimaryKeyException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 5:15 PM
 */
public class ValidationUtils {

    private ValidationUtils() {
        // not visible
    }

    public static void checkPrimary(Integer id) throws IncorrectPrimaryKeyException {
        if (id == null) {
            throw new IncorrectPrimaryKeyException("Value of primary key is null");
        }

        if (id < 0) {
            throw new IncorrectPrimaryKeyException("Value of primary key is negative");
        }

        if (id == 0) {
            throw new IncorrectPrimaryKeyException("Value of primary key is zero");
        }
    }

}
