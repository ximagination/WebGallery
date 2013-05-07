package persistence.dao.implementation;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Repository;
import persistence.dao.interfaces.FileManager;
import persistence.exception.PersistenceException;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 5:01 PM
 */
@Repository
public class FileManagerImpl implements FileManager {

    private final File path;

    public FileManagerImpl(String address) {
        this.path = new File(address);

        if (!path.exists()) {
            boolean isDirectoryCreated = path.mkdirs();

            if (!isDirectoryCreated) {
                throw new PersistenceException("File system can't create directory by path " + path);
            }
        }
    }

    @Override
    public File getFile(int id) {
        return new File(path, Integer.toString(id));
    }

    @Override
    public void createFile(byte[] image, int id) throws IOException {
        File file = getFile(id);

        OutputStream fos = null;
        try {
            /*
            Rewrite file if exists
             */
            fos = new FileOutputStream(file);

            /*
            Don't close ByteArrayInputStream. No needed.
             */
            IOUtils.copy(new ByteArrayInputStream(image), fos);
        } catch (FileNotFoundException e) {
            /*
            If the file exists but is a directory rather than a regular file,
            does not exist but cannot be created,
            or cannot be opened for any other reason then a FileNotFoundException is thrown.
            */
            throw e;

        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
}
