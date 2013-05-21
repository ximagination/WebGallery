package persistence.utils;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Projections;
import persistence.struct.Image;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/16/13
 * Time: 11:19 AM
 */
public class HibernateUtils {

    private HibernateUtils() {
        // not visible
    }

    public static int insert(Session session, Object objectForInsert) {
        try {
            session.save(objectForInsert);
            return 1;
        } catch (HibernateException e) {
            log(e);
            return 0;
        }
    }

    public static int update(Session session, Object objectForUpdate) {
        try {
            session.update(objectForUpdate);
            return 1;
        } catch (HibernateException e) {
            log(e);
            return 0;
        }
    }

    public static int delete(Session session, int id) {
        try {
            session.delete(session.get(Image.class, id));
            return 1;
        } catch (HibernateException e) {
            log(e);
            return 0;
        }
    }

    public static <T> Number getCount(Session session, Class<T> imageClass) {
        Criteria criteria = session.createCriteria(imageClass);
        criteria.setProjection(Projections.rowCount());
        return (Number) criteria.uniqueResult();
    }

    private static void log(HibernateException e) {
        e.printStackTrace();
    }
}
