package persistence.dao.hibernateImpl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.dao.abstractDAOImpl.AbstractImageDAO;
import persistence.exception.PersistenceException;
import persistence.struct.Image;
import persistence.utils.HibernateUtils;

import java.util.List;

@Repository
public class ImageDAOImpl extends AbstractImageDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    protected void insertImpl(Image image) {
        if (HibernateUtils.insert(getSession(), image) == 0) {
            throw new PersistenceException("Creating image failed, no rows affected after insert image with params " + image);
        }
    }

    @Override
    protected int updateImpl(Image image) {
        return HibernateUtils.update(getSession(), image);
    }

    @Override
    protected int deleteImpl(Integer id) {
        return HibernateUtils.delete(getSession(), Image.class, id);
    }

    @Override
    public List<Image> fetch() {
        return getSession().createCriteria(Image.class).list();
    }

    @Override
    protected Image fetchByPrimaryImpl(Integer id) {
        return (Image) getSession().get(Image.class, id);
    }

    @Override
    public int getCount() {
        return HibernateUtils.getCount(getSession(), Image.class).intValue();
    }

    @Override
    protected List<Image> fetchWithOffsetImpl(int offset, int limit) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Image.class);
        criteria.addOrder(Order.asc(getOrderColumn()));
        criteria.setMaxResults(limit);
        criteria.setFirstResult(offset);

        return criteria.list();
    }
}