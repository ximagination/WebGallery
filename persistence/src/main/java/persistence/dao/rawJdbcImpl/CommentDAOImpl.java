package persistence.dao.rawJdbcImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.connectAndSource.Connector;
import persistence.dao.abstractDAOImpl.AbstractCommentDAO;
import persistence.exception.ForeignKeyConstraintException;
import persistence.exception.IndexNotCreatedException;
import persistence.exception.PersistenceException;
import persistence.exception.ValidationException;
import persistence.struct.Comment;
import persistence.utils.DatabaseUtils;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 3:51 PM
 */
@Repository
public class CommentDAOImpl extends AbstractCommentDAO {

    // TABLE
    static final String TABLE_NAME = "comment";

    // FIELDS IN DATABASE
    static final String ID = "id";
    static final String IMAGE_ID = "imageId";
    static final String USER_ID = "userId";
    static final String TEXT = "text";
    static final String TIMESTAMP = "timestamp";

    // SQL
    private static final String INSERT = "INSERT INTO " + TABLE_NAME + "("
            + IMAGE_ID + ","
            + USER_ID + ","
            + TEXT + ") VALUES (?,?,?)";

    private static final String UPDATE = "UPDATE " + TABLE_NAME + " SET "
            + TEXT + "=?,"
            + TIMESTAMP + "=CURRENT_TIME() "
            + " WHERE " + ID + "=?";

    private static final String DELETE = "DELETE FROM " + TABLE_NAME
            + " WHERE " + ID + "=?";

    private static final String BY_PRIMARY = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + ID + "=?";

    private static final String FETCH = "SELECT * FROM " + TABLE_NAME;

    @Autowired
    private Connector connector;

    @PostConstruct
    protected void initScheme() throws PersistenceException {
        Connection c = getConnection();
        Statement stat = null;

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + IMAGE_ID + " INTEGER NOT NULL,"
                + USER_ID + " INTEGER NOT NULL ,"
                + TEXT + " VARCHAR(" + super.getMaxCommentLength() + ") NOT NULL ,"
                + TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + " FOREIGN KEY(" + IMAGE_ID + ") REFERENCES " + ImageDAOImpl.TABLE_NAME + "(" + ImageDAOImpl.ID + "),"
                + " FOREIGN KEY(" + USER_ID + ") REFERENCES " + UserDAOImpl.TABLE_NAME + "(" + UserDAOImpl.ID + ")"
                + ")";

        String creteIndex = "CREATE INDEX IF NOT EXISTS " + TABLE_NAME + "_" + IMAGE_ID + "_index ON " + TABLE_NAME + "(" + IMAGE_ID + ")";

        try {
            byte action = 0;
            try {
                c.setAutoCommit(false);
                stat = c.createStatement();
                /*
                Create table or throw
                 */
                action = 1;
                stat.execute(createTable);

                action = 2;
                stat.execute(creteIndex);
            } catch (SQLException e) {
                switch (action) {
                    case 0: {
                        throw new PersistenceException("Can't turn off autocommit flag or createStatement on " + TABLE_NAME, e);
                    }
                    case 1: {
                        throw new ForeignKeyConstraintException("Table " + UserDAOImpl.TABLE_NAME + "," + ImageDAOImpl.TABLE_NAME + " on which this table references to is not created. " +
                                "See stack trace for more details", e);
                    }
                    case 2: {
                        throw new IndexNotCreatedException("Table {" + createTable + "} created successfully but index{" + creteIndex + "} failed", e);
                    }
                    default:
                        throw new PersistenceException("Can't create table " + TABLE_NAME, e);
                }
            } finally {
                try {
                    c.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new PersistenceException("Can't setAutoCommit = true. Transaction failed?", e);
                }
            }
        } finally {
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    protected void insertImpl(Comment o) throws ValidationException {
        int imageId = o.getImageId();
        int userId = o.getUserId();
        String text = o.getComment();

        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet primaryKeySet = null;

        try {
            boolean isInserted;
            try {
                stat = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                stat.setInt(1, imageId);
                stat.setInt(2, userId);
                stat.setString(3, text);

                isInserted = stat.executeUpdate() > 0;
            } catch (SQLException e) {
                /*
                This exception occurs because in database was constraint
                when insert comment (imageId,userId,comment).
                By field userId or imageId.
                 */
                throw new ForeignKeyConstraintException("Can't insert new comment in table " + TABLE_NAME
                        + " with values imageId="
                        + imageId + ", userId="
                        + userId + ",comment=" + text, e);
            }

            if (!isInserted) {
                throw new PersistenceException("Creating comment failed, no rows affected after insert. SQL " + INSERT);
            }

            try {
                primaryKeySet = stat.getGeneratedKeys();
                if (primaryKeySet.next()) {
                    o.setId(primaryKeySet.getInt(1));
                } else {
                    throw new PersistenceException("Creating comment failed, no generated key obtained.");
                }
            } catch (SQLException e) {
                /*
                 Can't set primary key for user because JDBC driver throw SQLException.
                 */
                throw new PersistenceException("Creating comment failed, no generated key obtained.", e);
            }

        } finally {
            DatabaseUtils.closeQuietly(primaryKeySet);
            DatabaseUtils.closeQuietly(stat);
            DatabaseUtils.closeQuietly(c);
        }
    }

    @Override
    protected int updateImpl(Comment o) throws ValidationException {
        Connection c = getConnection();
        PreparedStatement stat = null;

        try {
            stat = c.prepareStatement(UPDATE);

            stat.setString(1, o.getComment());
            stat.setInt(2, o.getId());

            return stat.executeUpdate();
        } catch (SQLException e) {
            /*
            In update method user can change his comment.
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
    protected List<Comment> getCommentsForImageImpl(int id) throws ValidationException {
        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet rs = null;

        String o =
                " SELECT " +
                        "C." + ID + "," +
                        "C." + IMAGE_ID + "," +
                        "C." + USER_ID + "," +
                        "C." + TEXT + "," +
                        "C." + TIMESTAMP + "," +
                        "U." + UserDAOImpl.LOGIN + " " +
                        " FROM " + TABLE_NAME + " C " +
                        " INNER JOIN " + UserDAOImpl.TABLE_NAME + " U " +
                        " ON C." + USER_ID + " = U." + ID +
                        " AND C." + IMAGE_ID + " = ?" +
                        " ORDER BY C." + getOrderColumn() + " " + getOrderType();

        try {
            stat = c.prepareStatement(o);
            stat.setInt(1, id);

            rs = stat.executeQuery();

            List<Comment> result = new ArrayList<>();
            while (rs.next()) {
                result.add(readCommentAndUserName(rs));
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
    public List<Comment> fetch() {
        Connection c = getConnection();
        Statement stat = null;
        ResultSet rs = null;

        try {
            stat = c.createStatement();
            rs = stat.executeQuery(FETCH);

            List<Comment> result = new ArrayList<>();
            while (rs.next()) {
                result.add(readComment(rs));
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
    protected Comment fetchByPrimaryImpl(Integer id) throws ValidationException {
        Connection c = getConnection();
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            stat = c.prepareStatement(BY_PRIMARY);
            stat.setInt(1, id);

            rs = stat.executeQuery();

            Comment result = null;

            if (rs.next()) {
                result = readComment(rs);
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

    private static Comment readComment(ResultSet rs) throws SQLException {
        Comment o = new Comment();

        o.setId(rs.getInt(1));
        o.setImageId(rs.getInt(2));
        o.setUserId(rs.getInt(3));
        o.setComment(rs.getString(4));
        o.setTimestamp(rs.getTimestamp(5));

        return o;
    }

    private static Comment readCommentAndUserName(ResultSet rs) throws SQLException {
        Comment o = readComment(rs);
        o.setUserLogin(rs.getString(6));
        return o;
    }

    private Connection getConnection() {
        return connector.newConnection();
    }
}
