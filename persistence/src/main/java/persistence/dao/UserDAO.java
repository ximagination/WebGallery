package persistence.dao;

import persistence.exception.ValidateException;
import persistence.struct.User;

import java.util.List;

public interface UserDAO extends BaseDAO<User, Integer> {

    void insert(User user) throws ValidateException;

    void update(User user) throws ValidateException;

    void delete(Integer _id) throws ValidateException;

    List<User> fetch();

    User fetchByPrimary(Integer _id) throws ValidateException;

}