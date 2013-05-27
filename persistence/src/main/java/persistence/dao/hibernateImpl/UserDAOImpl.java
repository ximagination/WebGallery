package persistence.dao.hibernateImpl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.dao.abstractDAOImpl.AbstractUserDAO;
import persistence.exception.PersistenceException;
import persistence.struct.User;
import persistence.utils.HibernateUtils;

import java.util.List;

@Repository
public class UserDAOImpl extends AbstractUserDAO {

    @Autowired
    private SessionFactory sessionFactory;

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
        return HibernateUtils.delete(getSession(), User.class, id);
    }

    @Override
    public List<User> fetch() {
        return getSession().createCriteria(User.class).list();
    }

    @Override
    protected User fetchByPrimaryImpl(Integer id) {
        return (User) getSession().get(User.class, id);
    }

    @Override
    protected User fetchByLoginImpl(String login) {
        Session mSession = getSession();
        Criteria criteria = mSession.createCriteria(User.class);
        criteria.add(Restrictions.eq("login", login));
        criteria.setMaxResults(1);

        return (User) criteria.uniqueResult();
    }
}