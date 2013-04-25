package galleryService;

import galleryService.services.AutentificationService;
import galleryService.services.ImageService;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 4:46 PM
 */
public class ServiceHolder {

    private ServiceHolder() {
        // not visible
    }

    public static AutentificationService getAutentificationService() {
        return AutentificationService.getInstance();
    }

    public static ImageService getImageService() {
        return ImageService.getInstance();
    }

}
