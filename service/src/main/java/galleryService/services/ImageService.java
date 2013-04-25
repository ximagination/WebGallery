package galleryService.services;

import galleryService.exception.EmptyImageException;
import persistence.connectAndSource.DataSource;
import persistence.dao.factory.FileManagerFactory;
import persistence.dao.factory.ImageDAOFactory;
import persistence.dao.interfaces.FileManager;
import persistence.dao.interfaces.ImageDAO;
import persistence.exception.ValidationException;
import persistence.struct.Image;
import persistence.struct.User;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 6:03 PM
 */
public class ImageService {
    private static final ImageService INSTANCE = new ImageService();

    private ImageService() {
        // not visible
    }

    public static ImageService getInstance() {
        return INSTANCE;
    }

    public void addImage(User user, String name, String comment, byte[] data) throws ValidationException, IOException, RuntimeException {

        if (data == null || data.length == 0) {
            throw new EmptyImageException("param data must not be null");
        }

        Image image = new Image();
        image.setName(name);
        image.setComment(comment);
        image.setUserId(user.getId());

        ImageDAO dao = getImageDAO();
        dao.insert(image);

        int id = image.getId();
        try {
            getFileManager().createFile(data, id);
        } catch (IOException e) {
            dao.delete(id);

            throw e;
        }
    }

    private FileManager getFileManager() {
        return FileManagerFactory.getInstance();
    }

    private ImageDAO getImageDAO() {
        return ImageDAOFactory.getDao(DataSource.JDBC);
    }
}
