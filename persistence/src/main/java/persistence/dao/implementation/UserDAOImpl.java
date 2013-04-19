package persistence.dao.implementation;

import persistence.connectAndSource.Connector;
import persistence.dao.UserDAO;
import persistence.exception.*;
import persistence.struct.User;
import persistence.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    // TABLE
    private static final String TABLE_NAME = "user";

    // FIELDS IN DATABASE
    private static final String ID = "id";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    // SQL
    private static final int LOGIN_LIMIT = 32;
    private static final int PASSWORD_LIMIT = 32;

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTO_INCREMENT," +
            LOGIN + " VARCHAR(" + LOGIN_LIMIT + ") NOT NULL UNIQUE," + PASSWORD + " VARCHAR(" + PASSWORD_LIMIT + ") NOT NULL)";
    private static final String INSERT = "INSERT INTO " + TABLE_NAME + "(" +
            LOGIN + "," + PASSWORD + ") VALUES (?,?)";
    private static final String UPDATE = "UPDATE " + TABLE_NAME + " SET " + PASSWORD + "=? WHERE " + ID + "=?";
    private static final String DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + ID + "=?";
    private static final String FETCH = "SELECT * FROM " + TABLE_NAME;
    private static final String BY_PRIMARY = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=?";

    @Override
    public void create() throws PersistenceException {
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

        try {
            boolean isInserted;
            try {
                stat = c.prepareStatement(INSERT);

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

            /*
             User insert new login and password. Only login field contains constraints(Login is unique).
             This error occurs because user want register with login which already exists
             and JDBC driver don't throw JdbcSQLException or SQLException.
             */
            if (!isInserted) {
                throw new LoginAlreadyExistsException("Login " + login + " is busy.");
            }

        } finally {
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
                Unknown exception. Must not be occurs.
                */
                throw new PersistenceException(e);
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

            List<User> result = new ArrayList<User>();
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

            if (rs.next()) {
                return readUserFromResultSet(rs);
            }

            return null;

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
        return Connector.getInstance().newConnection();
    }

    private void validatePrimary(Integer id) throws IncorrectPrimaryKeyException {
        if (id == null) {
            throw new IncorrectPrimaryKeyException("Primary key for deleting single user is null");
        }

        if (id < 0) {
            throw new IncorrectPrimaryKeyException("Primary key for deleting single user is negative");
        }

        if (id == 0) {
            throw new IncorrectPrimaryKeyException("Primary key for deleting single user is 0");
        }
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