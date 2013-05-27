package persistence.dao.abstractDAOImpl;

import org.springframework.beans.factory.annotation.Value;
import persistence.dao.interfaces.CommentDAO;
import persistence.exception.*;
import persistence.struct.Comment;
import persistence.utils.ValidationUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 2:21 PM
 */
abstract public class AbstractCommentDAO implements CommentDAO {

    @Value(value = "${persistence.dao.Comment.commentMin}")
    private int commentMin;

    @Value(value = "${persistence.dao.Comment.commentLimit}")
    private int commentLimit;

    @Value(value = "${persistence.dao.Comment.orderColumn}")
    private String orderColumn;

    @Value(value = "${persistence.dao.Comment.orderType}")
    private String orderType;

    abstract protected void insertImpl(Comment o) throws ValidationException;

    abstract protected int updateImpl(Comment o) throws ValidationException;

    abstract protected int deleteImpl(Integer id) throws ValidationException;

    abstract protected List<Comment> getCommentsForImageImpl(int id) throws ValidationException;

    abstract protected Comment fetchByPrimaryImpl(Integer id) throws ValidationException;

    protected int getMinCommentLength() {
        return commentMin;
    }

    protected int getMaxCommentLength() {
        return commentLimit;
    }

    protected String getOrderColumn() {
        return orderColumn;
    }

    protected String getOrderType() {
        return orderType;
    }

    @Override
    public void insert(Comment o) throws ValidationException {
        validate(o.getImageId());
        validate(o.getUserId());
        validate(o.getComment());

        insertImpl(o);
    }

    @Override
    public void update(Comment o) throws ValidationException {
        validate(o.getId());
        validate(o.getComment());

        if (updateImpl(o) == 0) {
            throw new RecordNotFoundException("Record by id=" + o.getId() + " not found and comment can't be updated");
        }
    }

    @Override
    public void delete(Integer id) throws ValidationException {
        validate(id);

        if (deleteImpl(id) == 0) {
            throw new RecordNotFoundException("Record by id=" + id + " not found and comment can't be deleted");
        }
    }

    @Override
    public List<Comment> getCommentsForImage(int id) throws ValidationException {
        validate(id);

        return getCommentsForImageImpl(id);
    }

    @Override
    public Comment fetchByPrimary(Integer id) throws ValidationException {
        validate(id);
        return fetchByPrimaryImpl(id);
    }

    private void validate(Integer id) throws IncorrectPrimaryKeyException {
        ValidationUtils.checkPrimary(id);
    }

    private void validate(String text) throws ValidationException {
        if (text == null || text.isEmpty()) {
            throw new EmptyFieldException("Comment field is empty or null");
        }

        int length = text.length();

        int min = getMinCommentLength();
        if (min > length) {
            throw new ToShortFieldException("Field comment is very short. Min size of filed is " + min, min);
        }

        int max = getMaxCommentLength();
        if (max < length) {
            throw new ToLongFieldException("Field password is too long. Max size of filed is " + max, max);
        }
    }
}
