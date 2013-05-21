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
import persistence.dao.abstractDAOImpl.AbstractUserDAO;
import persistence.dao.interfaces.UserDAO;
import persistence.exception.PersistenceException;
import persistence.exception.RecordNotFoundException;
import persistence.struct.User;
import persistence.utils.DatabaseUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAOImpl extends AbstractUserDAO implements UserDAO {

    private NamedParameterJdbcOperations jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    // TABLE
    static final String TABLE_NAME = "user";

    // FIELDS IN DATABASE
    static final String ID = "id";
    static final String LOGIN = "login";
    static final String PASSWORD = "password";

    // SQL
    private static final String INSERT = "INSERT INTO " + TABLE_NAME + "("
            + LOGIN + ","
            + PASSWORD + ") VALUES (:" + LOGIN + ",:" + PASSWORD + ")";

    private static final String UPDATE = "UPDATE " + TABLE_NAME + " SET "
            + PASSWORD + "=:" + PASSWORD
            + " WHERE " + ID + "=:" + ID;

    private static final String DELETE = "DELETE FROM " + TABLE_NAME
            + " WHERE " + ID + "=:" + ID;

    private static final String FETCH = "SELECT * FROM " + TABLE_NAME;

    private static final String BY_PRIMARY = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + ID + "=:" + ID;

    private static final String BY_LOGIN = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + LOGIN + "=:" + LOGIN;

    private NamedParameterJdbcOperations getTemplate() {
        return jdbcTemplate;
    }

    private static final RowMapper<User> USER_ROW_MAPPER = new RowMapper<User>() {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();

            user.setId(rs.getInt(ID));
            user.setLogin(rs.getString(LOGIN));
            user.setPassword(rs.getString(PASSWORD));

            return user;
        }
    };

    @PostConstruct
    protected void initScheme() throws PersistenceException {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + LOGIN + " VARCHAR(" + super.getLoginLimit() + ") NOT NULL UNIQUE,"
                + PASSWORD + " VARCHAR(" + super.getPasswordLimit() + ") NOT NULL)";

        getTemplate().update(createTable, Collections.EMPTY_MAP);
    }

    @Override
    protected void insertImpl(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Map<String, Object> params = new HashMap<>(2, 1F);
        params.put(LOGIN, user.getLogin());
        params.put(PASSWORD, user.getPassword());

        SqlParameterSource parameterSource = new MapSqlParameterSource(params);

        int insertCount = getTemplate().update(INSERT, parameterSource, keyHolder);

        if (insertCount == 0) {
            throw new PersistenceException("Creating user failed, no rows affected after insert. SQL " + INSERT);
        }

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new PersistenceException("Creating user failed, no rows affected after insert. SQL " + INSERT);
        }

        user.setId(key.intValue());
    }

    @Override
    protected int updateImpl(User user) throws RecordNotFoundException {
        Map<String, Object> params = new HashMap<>(2, 1F);
        params.put(ID, user.getId());
        params.put(PASSWORD, user.getPassword());

        return getTemplate().update(UPDATE, params);
    }

    @Override
    protected int deleteImpl(Integer id) throws RecordNotFoundException {
        Map<String, Object> params = new HashMap<>(1, 1F);
        params.put(ID, id);

        return getTemplate().update(DELETE, params);
    }

    @Override
    public List<User> fetch() {
        return getTemplate().query(FETCH, Collections.EMPTY_MAP, USER_ROW_MAPPER);
    }


    @Override
    protected User fetchByPrimaryImpl(Integer id) {
        Map<String, Object> params = new HashMap<>(1, 1F);
        params.put(ID, id);

        return DatabaseUtils.getSingleRowOrNull(getTemplate(), BY_PRIMARY, params, USER_ROW_MAPPER);
    }

    @Override
    protected User fetchByLoginImpl(String login) {
        Map<String, Object> params = new HashMap<>(1, 1F);
        params.put(LOGIN, login);

        return DatabaseUtils.getSingleRowOrNull(getTemplate(), BY_LOGIN, params, USER_ROW_MAPPER);
    }
}