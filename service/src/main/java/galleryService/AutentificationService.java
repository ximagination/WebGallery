package galleryService;

import galleryService.exception.IncorrectPasswordException;
import galleryService.exception.LoginNotFoundException;
import persistence.connectAndSource.DataSource;
import persistence.dao.UserDAO;
import persistence.dao.factory.UserDAOFactory;
import persistence.exception.ValidationException;
import persistence.struct.User;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 4:48 PM
 */
public class AutentificationService {
    private static final AutentificationService INSTANCE = new AutentificationService();

    private AutentificationService() {
        // not visible
    }

    public static AutentificationService getInstance() {
        return INSTANCE;
    }

    /**
     * Create new record in database and autentificate user.
     *
     * @param login    - user login
     * @param password - user password
     * @return user object
     * @throws ValidationException
     * @throws RuntimeException
     */
    public User register(String login, String password) throws ValidationException, RuntimeException {
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
    public User autentificate(String login, String password) throws ValidationException, RuntimeException {

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
        return UserDAOFactory.getDao(DataSource.JDBC);
    }
}
