package persistence.dao.hibernateImpl;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.dao.abstractDAOImpl.AbstractImageDAO;
import persistence.dao.interfaces.ImageDAO;
import persistence.exception.PersistenceException;
import persistence.struct.Image;
import persistence.utils.HibernateUtils;

import java.util.List;

@Repository
public class ImageDAOImpl extends AbstractImageDAO implements ImageDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private static final String DELETE_HQL = "DELETE FROM image WHERE id=";

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
        return HibernateUtils.getCountOfQueryUpdate(getSession(), DELETE_HQL + id);
    }

    @Override
    public List<Image> fetch() {
        return HibernateUtils.getAll(getSession(), Image.class);
    }

    @Override
    protected Image fetchByPrimaryImpl(Integer id) {
        return HibernateUtils.getByPrimary(getSession(), Image.class, id);
    }
}