package persistence.dao.abstractDAOImpl;

import org.springframework.beans.factory.annotation.Value;
import persistence.dao.interfaces.UserDAO;
import persistence.exception.*;
import persistence.struct.User;
import persistence.utils.ValidationUtils;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/14/13
 * Time: 3:48 PM
 */
abstract public class AbstractUserDAO implements UserDAO {

    // LIMITS
    @Value(value = "${persistence.dao.User.loginLimit}")
    private int loginLimit;

    @Value(value = "${persistence.dao.User.passwordLimit}")
    private int passwordLimit;

    protected abstract void insertImpl(User user) throws ValidationException;

    protected abstract int updateImpl(User user) throws ValidationException;

    protected abstract int deleteImpl(Integer id) throws ValidationException;

    protected abstract User fetchByPrimaryImpl(Integer id);

    protected abstract User fetchByLoginImpl(String login);

    protected int getLoginLimit() {
        return loginLimit;
    }

    protected int getPasswordLimit() {
        return passwordLimit;
    }

    @Override
    public User fetchByLogin(String login) throws ValidationException {
        validateLogin(login);

        return fetchByLoginImpl(login);
    }

    @Override
    public void insert(User user) throws ValidationException {
        validateLogin(user.getLogin());
        validatePassword(user.getPassword());

        insertImpl(user);
    }

    @Override
    public void update(User user) throws ValidationException {
        validatePrimary(user.getId());
        validatePassword(user.getPassword());

        if (updateImpl(user) == 0) {
            throw new RecordNotFoundException("Record by id=" + user.getId() + " not found and user can't be updated");
        }
    }

    @Override
    public void delete(Integer id) throws ValidationException {
        validatePrimary(id);

        if (deleteImpl(id) == 0) {
            throw new RecordNotFoundException("Record by id=" + id + " not found and user can't be deleted");
        }
    }

    @Override
    public User fetchByPrimary(Integer id) throws ValidationException {
        validatePrimary(id);

        return fetchByPrimaryImpl(id);
    }

    private void validatePrimary(Integer id) throws IncorrectPrimaryKeyException {
        ValidationUtils.checkPrimary(id);
    }

    private void validateLogin(String login) throws ValidationException {
        if (login == null || login.isEmpty()) {
            throw new EmptyFieldException("Field login must not be empty.");
        }

        int loginLimit = getLoginLimit();
        if (login.length() > loginLimit) {
            throw new ToLongFieldException("Field login is too long. Max size of filed is " + loginLimit, loginLimit);
        }
    }

    private void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new EmptyFieldException("Field password must not be empty.");
        }

        int passwordLimit = getPasswordLimit();
        if (password.length() > passwordLimit) {
            throw new ToLongFieldException("Field password is too long. Max size of filed is " + passwordLimit, passwordLimit);
        }
    }
}
