package persistence.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.connectAndSource.Connector;
import persistence.dao.interfaces.ImageDAO;
import persistence.exception.*;
import persistence.struct.Image;
import persistence.utils.DatabaseUtils;
import persistence.utils.ValidationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageDAOImpl implements ImageDAO {

    // TABLE
    static final String TABLE_NAME = "image";

    // FIELDS IN DATABASE
    static final String ID = "id";
    static final String USER_ID = "userId";
    static final String NAME = "name";
    static final String COMMENT = "comment";
    static final String TIMESTAMP = "timestamp";

    // LIMITS
    private static final int NAME_LIMIT = 64;
    private static final int COMMENT_LIMIT = 255;

    // SQL
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
            + USER_ID + " INTEGER NOT NULL,"
            + NAME + " VARCHAR(" + NAME_LIMIT + ") NOT NULL ,"
            + COMMENT + " VARCHAR(" + COMMENT_LIMIT + ") NOT NULL DEFAULT '',"
            + TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            " FOREIGN KEY(" + USER_ID + ") REFERENCES " + UserDAOImpl.TABLE_NAME + "(" + UserDAOImpl.ID + "))";

    private static final String INSERT = "INSERT INTO " + TABLE_NAME + "("
            + USER_ID + ","
            + NAME + ","
            + COMMENT + ") VALUES (?,?,?)";

    private static final String UPDATE = "UPDATE " + TABLE_NAME + " SET "
            + NAME + "=?, "
            + COMMENT + "=?,"
            + TIMESTAMP + "=CURRENT_TIME() "
            + " WHERE " + ID + "=?";

    private static final String DELETE = "DELETE FROM " + TABLE_NAME
            + " WHERE " + ID + "=?";

    private static final String FETCH = "SELECT * FROM " + TABLE_NAME;

    private static final String BY_PRIMARY = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + ID + "=?";

    @Autowired
    private final Connector connector = null;

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
                User invoke method create() multiple times or table user is not created.
                 */
                throw new TableAlreadyExistsException("Table " + TABLE_NAME + " already exists " +
                        "or " +
                        "table " + UserDAOImpl.TABLE_NAME + " on which this table references to is not created. " +
                        "See stack trace for more details", e);
            }
        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    public void insert(Image image) throws ValidationException {

        int userId = image.getUserId();
        String name = image.getName();
        String comment = image.getComment();

        validatePrimary(userId);
        validateName(name);
        validateComment(comment);

        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet primaryKeySet = null;

        try {
            boolean isInserted;
            try {
                stat = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                stat.setInt(1, userId);
                stat.setString(2, name);
                stat.setString(3, comment);

                isInserted = stat.executeUpdate() > 0;
            } catch (SQLException e) {
                /*
                This exception occurs because in database was constraint
                when insert image (userId, name, comment).
                All validations by fields name and comment were produced ,
                but userId not found when we insert new record.
                 */
                throw new ForeignKeyConstraintException("Table " + UserDAOImpl.TABLE_NAME + " don't contains user with id=" + userId, e);
            }

            if (!isInserted) {
                throw new PersistenceException("Creating image failed, no rows affected after insert. SQL " + INSERT);
            }

            try {
                primaryKeySet = stat.getGeneratedKeys();
                if (primaryKeySet.next()) {
                    image.setId(primaryKeySet.getInt(1));
                } else {
                    throw new PersistenceException("Creating image failed, no generated key obtained.");
                }
            } catch (SQLException e) {
                /*
                 Can't set primary key for user because JDBC driver throw SQLException.
                 */
                throw new PersistenceException("Creating image failed, no generated key obtained.", e);
            }

        } finally {
            DatabaseUtils.closeQuietly(primaryKeySet);
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }


    @Override
    public void update(Image image) throws ValidationException {
        int id = image.getId();

        String name = image.getName();
        String comment = image.getComment();

        validatePrimary(id);
        validateName(name);
        validateComment(comment);

        Connection c = getConnection();
        PreparedStatement stat = null;

        try {
            boolean isUpdated;
            try {
                stat = c.prepareStatement(UPDATE);

                stat.setString(1, name);
                stat.setString(2, comment);

                isUpdated = stat.executeUpdate() > 0;
            } catch (SQLException e) {
                /*
                In update method user can change image name and comment.
                Constraint can't occurs because we already gone validate methods.
                Unknown exception. Must not be occurs.
                */
                throw new PersistenceException(e);
            }

            /*
            In update method user can change image name and comment.
            Constraint can't occurs because we already gone validate methods.
            This error occurs because user not found by id.
            */
            if (!isUpdated) {
                throw new RecordNotFoundException("Record by id=" + id + " not found and image can't be updated");
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
                throw new RecordNotFoundException("Record by id=" + id + " not found and image can't be deleted");
            }
        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    public List<Image> fetch() {
        Connection c = getConnection();
        Statement stat = null;
        ResultSet rs = null;

        try {
            stat = c.createStatement();
            rs = stat.executeQuery(FETCH);

            List<Image> result = new ArrayList<>();
            while (rs.next()) {
                result.add(readImageFromResultSet(rs));
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
    public Image fetchByPrimary(Integer id) throws ValidationException {
        validatePrimary(id);

        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            stat = c.prepareStatement(BY_PRIMARY);
            stat.setInt(1, id);

            rs = stat.executeQuery();

            Image result = null;

            if (rs.next()) {
                result = readImageFromResultSet(rs);
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

    private static Image readImageFromResultSet(ResultSet rs) throws SQLException {
        Image image = new Image();

        image.setId(rs.getInt(1));
        image.setUserId(rs.getInt(2));
        image.setName(rs.getString(3));
        image.setComment(rs.getString(4));
        image.setTimestamp(rs.getTimestamp(5));

        return image;
    }

    private Connection getConnection() {
        return connector.newConnection();
    }

    private void validatePrimary(Integer id) throws IncorrectPrimaryKeyException {
        ValidationUtils.checkPrimary(id);
    }

    private void validateName(String name) throws ValidationException {
        if (name == null || name.isEmpty()) {
            throw new EmptyFieldException("Field photo name must not be empty.");
        }

        if (name.length() > NAME_LIMIT) {
            throw new ToLongFieldException("Field photo name is too long. Max size of filed is " + NAME_LIMIT, NAME_LIMIT);
        }
    }

    private void validateComment(String comment) throws ValidationException {
        if (comment != null && comment.length() > NAME_LIMIT) {
            throw new ToLongFieldException("Field photo comment is too long. Max size of filed is " + COMMENT_LIMIT, COMMENT_LIMIT);
        }
    }
}