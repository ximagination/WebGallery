package galleryService.implementation;

import galleryService.exception.EmptyImageException;
import galleryService.interfaces.ImageService;
import galleryService.pojo.ImageInfoHolder;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.dao.interfaces.FileManager;
import persistence.dao.interfaces.ImageDAO;
import persistence.exception.ValidationException;
import persistence.struct.Image;
import persistence.struct.User;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 6:03 PM
 */
@Service
public class ImageServiceImpl implements ImageService {

    private ImageDAO imageDAO;
    private FileManager fileManager;

    @Value(value = "${service.imageCacheSize}")
    private float loadFactor;

    @Required
    public void setImageDAO(ImageDAO imageDAO) {
        this.imageDAO = imageDAO;
    }

    @Required
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Transactional
    @Override
    public void addImage(User user, String name, String comment, byte[] data) throws ValidationException, IOException {

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

    @Override
    public InputStream getImageById(int id) throws ValidationException, IOException {
        File file = getFileManager().getFile(id);

        long length = file.length();

        if (length == 0) {
            throw new IOException("File not found by id=" + id);
        }

        // calculate size of cache
        int cacheSize = (int) (length * loadFactor);

        return new BufferedInputStream(new FileInputStream(file), cacheSize);
    }

    @Transactional
    @Override
    public ImageInfoHolder getImages(int offset, int limit) throws ValidationException {
        ImageDAO dao = getImageDAO();

        int count = dao.getCount();
        List<Image> daoResult = dao.fetchWithOffset(offset, limit);

        List<Image> bindResult = (daoResult == null) ? Collections.EMPTY_LIST : daoResult;

        return new ImageInfoHolder(count, bindResult);
    }

    private FileManager getFileManager() {
        return fileManager;
    }

    private ImageDAO getImageDAO() {
        return imageDAO;
    }
}
