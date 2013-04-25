package persistence.dao.factory;


import persistence.dao.implementation.FileManagerImpl;
import persistence.dao.interfaces.FileManager;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 5:30 PM
 */
public class FileManagerFactory {

    private static final FileManager INSTANCE = new FileManagerImpl();

    private FileManagerFactory() {
        // not visible
    }

    public static FileManager getInstance() {
        return INSTANCE;
    }
}
