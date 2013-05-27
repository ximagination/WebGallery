package persistence.dao.jdbcTemplateImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import persistence.dao.abstractDAOImpl.AbstractCommentDAO;
import persistence.exception.PersistenceException;
import persistence.struct.Comment;
import persistence.utils.DatabaseUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 3:54 PM
 */
@Repository
public class CommentDAOImpl extends AbstractCommentDAO {

    private NamedParameterJdbcOperations jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

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
            + TEXT + ") VALUES (:" + IMAGE_ID + ",:" + USER_ID + ",:" + TEXT + ")";

    private static final String UPDATE = "UPDATE " + TABLE_NAME + " SET "
            + TEXT + "=:" + TEXT + ","
            + TIMESTAMP + "=CURRENT_TIME() "
            + " WHERE " + ID + "=:" + ID;

    private static final String DELETE = "DELETE FROM "
            + TABLE_NAME
            + " WHERE " + ID + "=:" + ID;

    private static final String FETCH = "SELECT * FROM "
            + TABLE_NAME;

    private static final String BY_PRIMARY = "SELECT * FROM "
            + TABLE_NAME
            + " WHERE "
            + ID + "=:" + ID;

    private NamedParameterJdbcOperations getTemplate() {
        return jdbcTemplate;
    }

    private static final RowMapper<Comment> COMMENT_ROW_MAPPER = new RowMapper<Comment>() {

        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment comment = new Comment();

            comment.setId(rs.getInt(ID));
            comment.setImageId(rs.getInt(IMAGE_ID));
            comment.setUserId(rs.getInt(USER_ID));
            comment.setComment(rs.getString(TEXT));
            comment.setTimestamp(rs.getTimestamp(TIMESTAMP));

            return comment;
        }
    };

    @PostConstruct
    protected void initScheme() throws PersistenceException {
        NamedParameterJdbcOperations template = getTemplate();

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

        template.update(createTable, Collections.EMPTY_MAP);
        template.update(creteIndex, Collections.EMPTY_MAP);
    }

    @Override
    protected void insertImpl(Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Map<String, Object> params = new HashMap<>(3, 1F);
        params.put(USER_ID, comment.getUserId());
        params.put(IMAGE_ID, comment.getImageId());
        params.put(TEXT, comment.getComment());

        SqlParameterSource parameterSource = new MapSqlParameterSource(params);

        int insertCount = getTemplate().update(INSERT, parameterSource, keyHolder);

        if (insertCount == 0) {
            throw new PersistenceException("Creating comment failed, no rows affected after insert. SQL " + INSERT);
        }

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new PersistenceException("Creating comment failed, no generated key obtained.");
        }

        comment.setId(key.intValue());
    }

    @Override
    protected int updateImpl(Comment comment) {
        Map<String, Object> params = new HashMap<>(2, 1F);
        params.put(ID, comment.getId());
        params.put(TEXT, comment.getComment());

        return getTemplate().update(UPDATE, params);
    }

    @Override
    protected int deleteImpl(Integer id) {
        Map<String, Object> params = new HashMap<>(1, 1F);
        params.put(ID, id);

        return getTemplate().update(DELETE, params);
    }

    @Override
    protected List<Comment> getCommentsForImageImpl(int imageId) {
        Map<String, Object> params = new HashMap<>(1, 1F);
        params.put(IMAGE_ID, imageId);

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
                        " AND C." + IMAGE_ID + " =:" + IMAGE_ID +
                        " ORDER BY C." + getOrderColumn() + " " + getOrderType();

        return getTemplate().query(o, params, COMMENT_ROW_MAPPER);
    }

    @Override
    protected Comment fetchByPrimaryImpl(Integer id) {
        Map<String, Object> params = new HashMap<>(1, 1F);
        params.put(ID, id);

        return DatabaseUtils.getSingleRowOrNull(getTemplate(), BY_PRIMARY, params, COMMENT_ROW_MAPPER);
    }

    @Override
    public List<Comment> fetch() {
        return getTemplate().query(FETCH, Collections.EMPTY_MAP, COMMENT_ROW_MAPPER);
    }
}
