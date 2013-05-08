package galleryService.implementation;

import galleryService.exception.IncorrectPasswordException;
import galleryService.exception.LoginNotFoundException;
import galleryService.interfaces.AutentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistence.dao.interfaces.UserDAO;
import persistence.exception.ValidationException;
import persistence.struct.User;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 4:48 PM
 */
@Service
public class AutentificationServiceImpl implements AutentificationService {

    @Autowired
    private UserDAO userDAO;

    /**
     * Create new record in database and autentificate user.
     *
     * @param login    - user login
     * @param password - user password
     * @return user object
     * @throws ValidationException
     * @throws RuntimeException
     */
    public User register(String login, String password) throws ValidationException {
        User user = new User();

        user.setLogin(login);
        user.setPassword(password);

        getUserDAO().insert(user);
        return user;
    }

    /**
     * Search user in database and autentificate if found.
     *
     * @param login    - user login
     * @param password - user password
     * @return user object
     * @throws ValidationException
     * @throws RuntimeException
     */
    public User autentificate(String login, String password) throws ValidationException {

        User user = getUserDAO().fetchByLogin(login);

        if (user == null) {
            throw new LoginNotFoundException("User with login '" + login + "' not registered.");
        }

        if (!user.getPassword().equals(password)) {
            throw new IncorrectPasswordException("Password '" + password + "' is incorrect.");
        }

        return user;
    }

    private UserDAO getUserDAO() {
        return userDAO;
    }
}
