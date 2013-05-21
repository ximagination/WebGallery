package galleryService.interfaces;

import galleryService.pojo.ImageInfoHolder;
import persistence.exception.ValidationException;
import persistence.struct.User;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 4:51 PM
 */
public interface ImageService {

    void addImage(User user, String name, String comment, byte[] data) throws ValidationException, IOException;

    InputStream getImageById(int id) throws ValidationException, IOException;

    ImageInfoHolder getImages(int offset, int limit) throws ValidationException;
}
