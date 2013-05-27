package persistence.dao.hibernateImpl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.dao.abstractDAOImpl.AbstractCommentDAO;
import persistence.exception.PersistenceException;
import persistence.exception.ValidationException;
import persistence.struct.Comment;
import persistence.utils.HibernateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 3:54 PM
 */
@Repository
public class CommentDAOImpl extends AbstractCommentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    protected void insertImpl(Comment comment) {
        if (HibernateUtils.insert(getSession(), comment) == 0) {
            throw new PersistenceException("Creating comment failed, no rows affected after insert comment with params " + comment);
        }
    }

    @Override
    protected int updateImpl(Comment comment) {
        return HibernateUtils.update(getSession(), comment);
    }

    @Override
    protected int deleteImpl(Integer id) {
        return HibernateUtils.delete(getSession(), Comment.class, id);
    }

    @Override
    protected List<Comment> getCommentsForImageImpl(int imageId) throws ValidationException {
        String o =
                " SELECT " +
                        "C.id," +
                        "C.imageId," +
                        "C.userId," +
                        "C.text," +
                        "C.timestamp," +
                        "U.login" +
                        " FROM comment C " +
                        " INNER JOIN user U " +
                        " ON C.userId = U.id" +
                        " AND C.imageId = :imageId" +
                        " ORDER BY C." + getOrderColumn() + " " + getOrderType();

        Query query = getSession().createQuery(o);
        query.setParameter(1, imageId);

        List<Comment> comments = new ArrayList<>();

        Iterator itr = query.iterate();
        while (itr.hasNext()) {
            Object items[] = (Object[]) itr.next();

            Comment comment = new Comment();
            comment.setId((Integer) items[0]);
            comment.setImageId((Integer) items[1]);
            comment.setUserId((Integer) items[2]);
            comment.setComment((String) items[3]);
            comment.setTimestamp((Date) items[4]);
            comment.setUserLogin((String) items[5]);

            comments.add(comment);
        }
        return comments;
    }

    @Override
    public List<Comment> fetch() {
        return getSession().createCriteria(Comment.class).list();
    }

    @Override
    protected Comment fetchByPrimaryImpl(Integer id) {
        return (Comment) getSession().get(Comment.class, id);
    }
}
