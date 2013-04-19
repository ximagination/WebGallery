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
     * @param typeOfSource id of database
     * @return DAO of user
     * @throws RuntimeException if unknown source
     * @see DataSource
     */
    public static UserDAO getDao(int typeOfSource) {
        switch (typeOfSource) {
            case DataSource.H2:
                return new UserDAOImpl();

            default:
                throw new UnknownDataSourceException("unknown source with id=" + typeOfSource + ".");
        }
    }

}
