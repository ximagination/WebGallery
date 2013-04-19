package persistence.utils;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 2:23 PM
 */
public class DatabaseUtils {

    private DatabaseUtils() {
        // not visible
    }

    public static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                // ignored
            }
        }
    }
}
