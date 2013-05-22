package persistence.dao.fileManager;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import persistence.dao.interfaces.FileManager;
import persistence.exception.PersistenceException;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 5:01 PM
 */
@Repository
public class FileManagerImpl implements FileManager {

    @Value(value = "${persistence.fileManager.pathToOriginalImages}")
    private File pathToOriginalImages;

    @Value(value = "${persistence.fileManager.pathToPreviewImages}")
    private File pathToPreviewImages;

    @PostConstruct
    public void init() {
        cretePathOrThrow(pathToOriginalImages, "ORIGINAL");
        cretePathOrThrow(pathToPreviewImages, "SCALED");
    }

    private void cretePathOrThrow(File path, String type) {
        if (!path.exists() && !path.mkdirs()) {
            throw new PersistenceException("File system can't create directory " + path + " for " + type + " images");
        }
    }

    @Override
    public File getFile(int id, boolean isOriginal) {
        File path = isOriginal ? pathToOriginalImages : pathToPreviewImages;

        return new File(path, Integer.toString(id));
    }

    @Override
    public void createFile(byte[] image, int id, boolean isOriginal) throws IOException {
        File file = getFile(id, isOriginal);

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
