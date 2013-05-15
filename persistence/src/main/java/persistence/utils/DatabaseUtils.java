package persistence.utils;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/19/13
 * Time: 2:23 PM
 */
public class DatabaseUtils {

    private DatabaseUtils() {
        // not visible
    }

    public static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                // ignored
            }
        }
    }

    public static <T> T getSingleRowOrNull(NamedParameterJdbcOperations ops, String sql, Map<String, ?> params, RowMapper<T> rowMapper) {
        if (ops == null) {
            throw new NullPointerException("NamedParameterJdbcOperations must not be null");
        }
        if (sql == null) {
            throw new NullPointerException("SQL must not be null");
        }
        if (params == null) {
            throw new NullPointerException("Map<String, ?> must not be null");
        }
        if (rowMapper == null) {
            throw new NullPointerException("RowMapper<T> must not be null");
        }

        try {
            return ops.queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
