package persistence.dao;

import persistence.exception.PersistenceException;
import persistence.exception.ValidationException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/18/13
 * Time: 10:45 AM
 */
public interface BaseDAO<T, K> {

    void create() throws PersistenceException;

    void insert(T o) throws ValidationException;

    void update(T o) throws ValidationException;

    void delete(K id) throws ValidationException;

    List<T> fetch();

    T fetchByPrimary(K id) throws ValidationException;
}
