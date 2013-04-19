package persistence.connectAndSource;

import persistence.exception.DriverClassNotFoundException;
import persistence.exception.NoConnectionToDatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/17/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Connector {

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverClassNotFoundException("Driver 'org.h2.Driver' not found ", e);
        }
    }

    private static final String URL = "jdbc:h2:~/test";
    private static final Connector INSTANCE = new Connector();

    private Connector() {
        // not visible
    }

    /**
     * @return instance
     */
    public static Connector getInstance() {
        return INSTANCE;
    }

    /**
     * Instantiate connection
     *
     * @return new connection to h2 database
     * @throws RuntimeException if SQLException occurs
     */
    public Connection newConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new NoConnectionToDatabaseException("Can't instantiate connection to '" + URL + "' database ", e);
        }
    }
}
