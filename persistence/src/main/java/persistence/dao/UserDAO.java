package persistence.dao;

import persistence.exception.ValidationException;
import persistence.struct.User;

public interface UserDAO extends BaseDAO<User, Integer> {

    User fetchByLogin(String login) throws ValidationException;

}