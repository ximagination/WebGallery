package persistence.utils;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Projections;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/16/13
 * Time: 11:19 AM
 */
public class HibernateUtils {

    //Logger
    private static final Logger LOGGER = Logger.getLogger(HibernateUtils.class);

    private HibernateUtils() {
        // not visible
    }

    public static int insert(Session session, Object objectForInsert) {
        try {
            session.save(objectForInsert);
            return 1;
        } catch (HibernateException e) {
            LOGGER.error("Insert Exception. Record can't be inserted for object=" + objectForInsert
                    + " for class " + objectForInsert.getClass() + " on session " + session
                    + ". Method insert return 0.", e);
            return 0;
        }
    }

    public static int update(Session session, Object objectForUpdate) {
        try {
            session.update(objectForUpdate);
            return 1;
        } catch (HibernateException e) {
            LOGGER.error("Update Exception. Record can't be updated for object=" + objectForUpdate
                    + " for class " + objectForUpdate.getClass() + " on session " + session
                    + ". Method update return 0.", e);
            return 0;
        }
    }

    public static <T> int delete(Session session, Class<T> cls, int id) {
        try {
            session.delete(session.get(cls, id));
            return 1;
        } catch (HibernateException e) {
            LOGGER.error("Delete Exception. Record not found by id=" + id
                    + " for class " + cls + " on session " + session
                    + ". Method delete return 0.", e);
            return 0;
        }
    }

    public static <T> Number getCount(Session session, Class<T> imageClass) {
        Criteria criteria = session.createCriteria(imageClass);
        criteria.setProjection(Projections.rowCount());
        return (Number) criteria.uniqueResult();
    }
}
