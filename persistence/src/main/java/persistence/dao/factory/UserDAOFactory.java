package persistence.dao.factory;

import persistence.connectAndSource.DataSource;
import persistence.dao.implementation.UserDAOImpl;
import persistence.dao.interfaces.UserDAO;
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

    private static final UserDAO USER_DAO = new UserDAOImpl();

    /**
     * @param typeOfSource type of database
     * @return DAO of user associated with typeOfSource
     * @throws UnknownDataSourceException if unknown source
     * @see DataSource
     */
    public static UserDAO getDao(DataSource typeOfSource) {
        switch (typeOfSource) {
            case JDBC:
                /*
                Don't create new instance of object.
                Just return reference to created object.
                 */
                return USER_DAO;

            default:
                throw new UnknownDataSourceException("unknown source with typeOfSource=" + typeOfSource + ".");
        }
    }

}
