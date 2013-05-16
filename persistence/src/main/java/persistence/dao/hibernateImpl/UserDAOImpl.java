package persistence.dao.hibernateImpl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.dao.abstractDAOImpl.AbstractUserDAO;
import persistence.dao.interfaces.UserDAO;
import persistence.exception.PersistenceException;
import persistence.struct.User;
import persistence.utils.HibernateUtils;

import java.util.List;

@Repository
public class UserDAOImpl extends AbstractUserDAO implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private static final String DELETE_SQL = "DELETE FROM user WHERE id=";

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    protected void insertImpl(User user) {
        if (HibernateUtils.insert(getSession(), user) == 0) {
            throw new PersistenceException("Creating user failed, no rows affected after insert user with params " + user);
        }
    }

    @Override
    protected int updateImpl(User user) {
        return HibernateUtils.update(getSession(), user);
    }

    @Override
    protected int deleteImpl(Integer id) {
        return HibernateUtils.getCountOfQueryUpdate(getSession(), DELETE_SQL + id);
    }

    @Override
    public List<User> fetch() {
        return HibernateUtils.getAll(getSession(), User.class);
    }

    @Override
    protected User fetchByPrimaryImpl(Integer id) {
        return HibernateUtils.getByPrimary(getSession(), User.class, id);
    }

    @Override
    protected User fetchByLoginImpl(String login) {
        Session mSession = getSession();
        try {
            Criteria criteria = mSession.createCriteria(User.class);
            criteria.add(Restrictions.eq("login", login));
            criteria.setMaxResults(1);

            List result = criteria.list();
            User user = null;

            if (!(result == null || result.isEmpty())) {
                user = (User) result.get(0);
            }

            return user;
        } finally {
            mSession.close();
        }
    }
}