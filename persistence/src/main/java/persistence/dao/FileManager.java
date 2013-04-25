package persistence.dao;

import persistence.dao.implementation.FileManagerDAOImpl;
import persistence.dao.interfaces.FileManagerDAO;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 5:30 PM
 */
public class FileManager {

    private static final FileManagerDAO INSTANCE = new FileManagerDAOImpl();

    private FileManager() {
        // not visible
    }

    public static FileManagerDAO getInstance() {
        return INSTANCE;
    }
}
