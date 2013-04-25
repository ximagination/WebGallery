package persistence.dao.factory;

import persistence.connectAndSource.DataSource;
import persistence.dao.implementation.ImageDAOImpl;
import persistence.dao.interfaces.ImageDAO;
import persistence.exception.UnknownDataSourceException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 12:26 PM
 */
public class ImageDAOFactory {

    private ImageDAOFactory() {
        // not visible
    }

    private static final ImageDAO IMAGE_DAO = new ImageDAOImpl();

    /**
     * @param typeOfSource type of database
     * @return DAO of user associated with typeOfSource
     * @throws persistence.exception.UnknownDataSourceException
     *          if unknown source
     * @see persistence.connectAndSource.DataSource
     */
    public static ImageDAO getDao(DataSource typeOfSource) {
        switch (typeOfSource) {
            case JDBC:
                /*
                Don't create new instance of object.
                Just return reference to created object.
                 */
                return IMAGE_DAO;

            default:
                throw new UnknownDataSourceException("unknown source with typeOfSource=" + typeOfSource + ".");
        }
    }

}
