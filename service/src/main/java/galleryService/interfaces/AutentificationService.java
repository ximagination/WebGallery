package galleryService.interfaces;

import persistence.exception.ValidationException;
import persistence.struct.User;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 10:45 AM
 */
public interface AutentificationService {

    User register(String login, String password) throws ValidationException;

    User autentificate(String login, String password) throws ValidationException;

}
