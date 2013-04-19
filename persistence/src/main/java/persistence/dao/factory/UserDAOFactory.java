package persistence.dao.factory;

import persistence.connectAndSource.DataSource;
import persistence.dao.UserDAO;
import persistence.dao.implementation.UserDAOImpl;
import persistence.exception.UnknownDataSourceException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 12:26 PM
 */
public class UserDAOFactory {

    private UserDAOFactory() {
        // not visible
    }

    /**
     * @param typeOfSource type of database
     * @return DAO of user associated with typeOfSource
     * @throws UnknownDataSourceException if unknown source
     * @see DataSource
     */
    public static UserDAO getDao(DataSource typeOfSource) {
        switch (typeOfSource) {
            case JDBC:
                return new UserDAOImpl();

            default:
                throw new UnknownDataSourceException("unknown source with typeOfSource=" + typeOfSource + ".");
        }
    }

}
