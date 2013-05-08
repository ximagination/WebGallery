package persistence.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.connectAndSource.Connector;
import persistence.dao.interfaces.UserDAO;
import persistence.exception.*;
import persistence.struct.User;
import persistence.utils.DatabaseUtils;
import persistence.utils.ValidationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    // TABLE
    static final String TABLE_NAME = "user";

    // FIELDS IN DATABASE
    static final String ID = "id";
    static final String LOGIN = "login";
    static final String PASSWORD = "password";

    // LIMITS
    private static final int LOGIN_LIMIT = 32;
    private static final int PASSWORD_LIMIT = 32;

    // SQL
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
            + LOGIN + " VARCHAR(" + LOGIN_LIMIT + ") NOT NULL UNIQUE,"
            + PASSWORD + " VARCHAR(" + PASSWORD_LIMIT + ") NOT NULL)";

    private static final String INSERT = "INSERT INTO " + TABLE_NAME + "("
            + LOGIN + ","
            + PASSWORD + ") VALUES (?,?)";

    private static final String UPDATE = "UPDATE " + TABLE_NAME + " SET "
            + PASSWORD + "=? "
            + " WHERE " + ID + "=?";

    private static final String DELETE = "DELETE FROM " + TABLE_NAME
            + " WHERE " + ID + "=?";

    private static final String FETCH = "SELECT * FROM " + TABLE_NAME;

    private static final String BY_PRIMARY = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + ID + "=?";

    private static final String BY_LOGIN = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + LOGIN + "=?";

    @Autowired
    private Connector connector = null;

    @Override
    public void initScheme() throws PersistenceException {
        Connection c = getConnection();
        Statement stat = null;

        try {
            try {
                stat = c.createStatement();

                /*
                Create table or throw
                 */
                stat.execute(CREATE_TABLE);
            } catch (SQLException e) {
                /*
                User invoke method create() multiple times. Table already exists.
                 */
                throw new TableAlreadyExistsException(e);
            }
        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    public void insert(User user) throws ValidationException {

        String login = user.getLogin();
        String password = user.getPassword();

        validateLogin(login);
        validatePassword(password);

        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet primaryKeySet = null;

        try {
            boolean isInserted;
            try {
                stat = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                stat.setString(1, login);
                stat.setString(2, password);

                isInserted = stat.executeUpdate() > 0;
            } catch (SQLException e) {
                /*
                This exception occurs because in database was constraint
                when insert user (login,password).
                Login is unique and user try to get already exists login.
                We throw validate exception.
                 */
                throw new LoginAlreadyExistsException("Login " + login + " is busy.", e);
            }

            if (!isInserted) {
                throw new PersistenceException("Creating user failed, no rows affected after insert. SQL " + INSERT);
            }

            try {
                primaryKeySet = stat.getGeneratedKeys();
                if (primaryKeySet.next()) {
                    user.setId(primaryKeySet.getInt(1));
                } else {
                    throw new PersistenceException("Creating user failed, no generated key obtained.");
                }
            } catch (SQLException e) {
                /*
                 Can't set primary key for user because JDBC driver throw SQLException.
                 */
                throw new PersistenceException("Creating user failed, no generated key obtained.", e);
            }

        } finally {
            DatabaseUtils.closeQuietly(primaryKeySet);
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    public void update(User user) throws ValidationException {
        int id = user.getId();
        String login = user.getLogin();
        String password = user.getPassword();

        validatePrimary(id);
        validateLogin(login);
        validatePassword(password);

        Connection c = getConnection();
        PreparedStatement stat = null;

        try {
            boolean isUpdated;
            try {
                stat = c.prepareStatement(UPDATE);

                stat.setString(1, password);
                stat.setInt(2, id);

                isUpdated = stat.executeUpdate() > 0;
            } catch (SQLException e) {
                /*
                Unknown exception. Must not be occurs.
                */
                throw new PersistenceException(e);
            }

             /*
             In update method user can't change login and constraint can't occurs by login field(Login is unique).
             This error occurs because user not found by id
             */
            if (!isUpdated) {
                throw new RecordNotFoundException("Record by id=" + id + " not found and user can't be updated");
            }

        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    public void delete(Integer id) throws ValidationException {
        validatePrimary(id);

        Connection c = getConnection();
        PreparedStatement stat = null;

        try {
            boolean isDeleted;
            try {
                stat = c.prepareStatement(DELETE);

                stat.setInt(1, id);

                isDeleted = stat.executeUpdate() > 0;
            } catch (SQLException e) {
                /*
                 This exception occurs because this user have some images or other records.
                 Firstly remove this items and then user record.
                 */
                throw new ForeignKeyConstraintException("User can't be remove by id=" + id, e);
            }

            if (!isDeleted) {
                throw new RecordNotFoundException("Record by id=" + id + " not found and user can't be deleted");
            }

        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    public List<User> fetch() {
        Connection c = getConnection();
        Statement stat = null;
        ResultSet rs = null;

        try {
            stat = c.createStatement();
            rs = stat.executeQuery(FETCH);

            List<User> result = new ArrayList<>();
            while (rs.next()) {
                result.add(readUserFromResultSet(rs));
            }
            return result;

        } catch (SQLException e) {
             /*
            Unknown exception. Must not be occurs.
             */
            throw new PersistenceException(e);

        } finally {
            DatabaseUtils.closeQuietly(rs);
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    private static User readUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt(1));
        user.setLogin(rs.getString(2));
        user.setPassword(rs.getString(3));

        return user;
    }

    @Override
    public User fetchByPrimary(Integer id) throws IncorrectPrimaryKeyException {
        validatePrimary(id);

        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            stat = c.prepareStatement(BY_PRIMARY);
            stat.setInt(1, id);

            rs = stat.executeQuery();

            User result = null;

            if (rs.next()) {
                result = readUserFromResultSet(rs);
            }

            return result;

        } catch (SQLException e) {
            /*
            Unknown exception. Must not be occurs.
             */
            throw new PersistenceException(e);

        } finally {
            DatabaseUtils.closeQuietly(rs);
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    public User fetchByLogin(String login) throws ValidationException {
        validateLogin(login);

        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            stat = c.prepareStatement(BY_LOGIN);
            stat.setString(1, login);

            rs = stat.executeQuery();

            User result = null;

            if (rs.next()) {
                result = readUserFromResultSet(rs);
            }

            return result;

        } catch (SQLException e) {
            /*
            Unknown exception. Must not be occurs.
             */
            throw new PersistenceException(e);

        } finally {
            DatabaseUtils.closeQuietly(rs);
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    private Connection getConnection() {
        return connector.newConnection();
    }

    private void validatePrimary(Integer id) throws IncorrectPrimaryKeyException {
        ValidationUtils.checkPrimary(id);
    }

    private void validateLogin(String login) throws ValidationException {
        if (login == null || login.isEmpty()) {
            throw new EmptyFieldException("Field login must not be empty.");
        }

        if (login.length() > LOGIN_LIMIT) {
            throw new ToLongFieldException("Field login is too long. Max size of filed is " + LOGIN_LIMIT, LOGIN_LIMIT);
        }
    }

    private void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new EmptyFieldException("Field password must not be empty.");
        }

        if (password.length() > PASSWORD_LIMIT) {
            throw new ToLongFieldException("Field password is too long. Max size of filed is " + PASSWORD_LIMIT, PASSWORD_LIMIT);
        }
    }
}