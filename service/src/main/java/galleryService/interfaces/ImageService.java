package galleryService.interfaces;

import persistence.exception.ValidationException;
import persistence.struct.User;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/24/13
 * Time: 4:51 PM
 */
public interface ImageService {

    public void addImage(User user, String name, String comment, byte[] data) throws ValidationException, IOException;

}
