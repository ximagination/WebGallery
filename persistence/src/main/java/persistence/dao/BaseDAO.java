package persistence.dao;

import persistence.exception.ValidateException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 10:45 AM
 */
public interface BaseDAO<T, K> {
    void insert(T o) throws ValidateException;

    void update(T o) throws ValidateException;

    void delete(K id) throws ValidateException;

    List<T> fetch();

    T fetchByPrimary(K id) throws ValidateException;
}
