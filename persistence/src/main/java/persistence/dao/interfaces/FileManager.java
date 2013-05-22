package persistence.dao.interfaces;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 4:51 PM
 */
public interface FileManager {

    File getFile(int id, boolean isOriginal);

    void createFile(byte[] image, int id,boolean isOriginal) throws IOException;

}
