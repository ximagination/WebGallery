package persistence.dao.rawJdbcImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.connectAndSource.Connector;
import persistence.dao.abstractDAOImpl.AbstractImageDAO;
import persistence.dao.interfaces.ImageDAO;
import persistence.exception.ForeignKeyConstraintException;
import persistence.exception.PersistenceException;
import persistence.exception.TableAlreadyExistsException;
import persistence.exception.ValidationException;
import persistence.struct.Image;
import persistence.utils.DatabaseUtils;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageDAOImpl extends AbstractImageDAO implements ImageDAO {

    // TABLE
    static final String TABLE_NAME = "image";

    // FIELDS IN DATABASE
    static final String ID = "id";
    static final String USER_ID = "userId";
    static final String NAME = "name";
    static final String COMMENT = "comment";
    static final String TIMESTAMP = "timestamp";

    // SQL
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
    private Connector connector;

    @PostConstruct
    protected void initScheme() throws PersistenceException {
        Connection c = getConnection();
        Statement stat = null;

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + USER_ID + " INTEGER NOT NULL,"
                + NAME + " VARCHAR(" + super.getNameLimit() + ") NOT NULL ,"
                + COMMENT + " VARCHAR(" + super.getCommentLimit() + ") NOT NULL DEFAULT '',"
                + TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                " FOREIGN KEY(" + USER_ID + ") REFERENCES " + UserDAOImpl.TABLE_NAME + "(" + UserDAOImpl.ID + "))";

        try {
            stat = c.createStatement();

                /*
                Create table or throw
                 */
            stat.execute(createTable);
        } catch (SQLException e) {
                /*
                User invoke method create() multiple times or table user is not created.
                 */
            throw new TableAlreadyExistsException("Table " + TABLE_NAME + " already exists " +
                    "or " +
                    "table " + UserDAOImpl.TABLE_NAME + " on which this table references to is not created. " +
                    "See stack trace for more details", e);
        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    protected void insertImpl(Image image) throws ValidationException {
        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet primaryKeySet = null;

        try {
            boolean isInserted;
            try {
                stat = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                stat.setInt(1, image.getUserId());
                stat.setString(2, image.getName());
                stat.setString(3, image.getComment());

                isInserted = stat.executeUpdate() > 0;
            } catch (SQLException e) {
                /*
                This exception occurs because in database was constraint
                when insert image (userId, name, comment).
                All validations by fields name and comment were produced ,
                but userId not found when we insert new record.
                 */
                throw new ForeignKeyConstraintException("Table " + UserDAOImpl.TABLE_NAME + " don't contains user with id=" + image.getUserId(), e);
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
    protected int updateImpl(Image image) throws ValidationException {
        Connection c = getConnection();
        PreparedStatement stat = null;

        try {
            stat = c.prepareStatement(UPDATE);

            stat.setString(1, image.getName());
            stat.setString(2, image.getComment());
            stat.setInt(3, image.getId());

            return stat.executeUpdate();
        } catch (SQLException e) {
            /*
            In update method user can change image name and comment.
            Constraint can't occurs because we already gone validate methods.
            Unknown exception. Must not be occurs.
            */
            throw new PersistenceException(e);
        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    protected int deleteImpl(Integer id) throws ValidationException {
        Connection c = getConnection();
        PreparedStatement stat = null;

        try {
            stat = c.prepareStatement(DELETE);

            stat.setInt(1, id);

            return stat.executeUpdate();
        } catch (SQLException e) {
            /*
            Unknown exception. Must not be occurs.
            */
            throw new PersistenceException(e);
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
    protected Image fetchByPrimaryImpl(Integer id) {
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
}