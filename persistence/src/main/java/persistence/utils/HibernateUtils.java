package persistence.utils;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import java.io.Serializable;
import java.util.List;

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
        checkSession(session);
        checkTransaction(session);
        notNull(objectForInsert, "objectForInsert");

        int resultOfInsert = 1;

        try {
            session.save(objectForInsert);
        } catch (HibernateException e) {
            log(e);

            resultOfInsert = 0;
        } finally {
            session.close();
        }

        return resultOfInsert;
    }

    public static int update(Session session, Object objectForUpdate) {
        checkSession(session);
        checkTransaction(session);
        notNull(objectForUpdate, "objectForUpdate");

        int resultOfUpdate = 1;

        try {
            session.update(objectForUpdate);
        } catch (HibernateException e) {
            log(e);

            resultOfUpdate = 0;
        } finally {
            session.close();
        }

        return resultOfUpdate;
    }


    public static <T> List<T> getAll(Session session, Class<T> cls) {
        checkSession(session);
        notNull(cls, "cls");

        try {
            return session.createCriteria(cls).list();
        } finally {
            session.close();
        }
    }

    public static <T> T getByPrimary(Session session, Class<T> cls, Serializable id) {
        checkSession(session);
        notNull(cls, "cls");
        notNull(id, "id");

        try {
            return (T) session.get(cls, id);
        } finally {
            session.close();
        }
    }

    public static int getCountOfQueryUpdate(Session session, String hql) {
        checkSession(session);
        notNull(hql, "id");

        try {
            return session.createQuery(hql).executeUpdate();
        } finally {
            session.close();
        }
    }

    private static void log(HibernateException e) {
        e.printStackTrace();
    }

    private static void checkTransaction(Session session) {
        if (!session.getTransaction().isActive()) {
            throw new IllegalArgumentException("Transaction not available");
        }
    }

    private static <T> void notNull(Object o, String name) {
        if (o == null) {
            throw new IllegalArgumentException("Input param " + name + " must not be null");
        }
    }

    private static void checkSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null");
        }
        if (!session.isOpen()) {
            throw new IllegalArgumentException("Session is closed");
        }
    }
}
