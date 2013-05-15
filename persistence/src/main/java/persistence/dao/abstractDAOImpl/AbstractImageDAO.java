package persistence.dao.abstractDAOImpl;

import org.springframework.beans.factory.annotation.Value;
import persistence.dao.interfaces.ImageDAO;
import persistence.exception.*;
import persistence.struct.Image;
import persistence.utils.ValidationUtils;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/14/13
 * Time: 3:47 PM
 */
abstract public class AbstractImageDAO implements ImageDAO {

    // LIMITS
    @Value(value = "${persistence.dao.Image.nameLimit}")
    private int nameLimit;

    @Value(value = "${persistence.dao.Image.commentLimit}")
    private int commentLimit;

    protected abstract void insertImpl(Image image) throws ValidationException;

    protected abstract int updateImpl(Image image) throws ValidationException;

    protected abstract int deleteImpl(Integer id) throws ValidationException;

    protected abstract Image fetchByPrimaryImpl(Integer id);

    protected int getNameLimit() {
        return nameLimit;
    }

    protected int getCommentLimit() {
        return commentLimit;
    }

    public void insert(Image image) throws ValidationException {
        validatePrimary(image.getUserId());
        validateName(image.getName());
        validateComment(image.getComment());

        insertImpl(image);
    }

    public void update(Image image) throws ValidationException {
        validatePrimary(image.getId());
        validateName(image.getName());
        validateComment(image.getComment());

        if (updateImpl(image) == 0) {
            throw new RecordNotFoundException("Record by id=" + image.getId() + " not found and image can't be updated");
        }
    }

    public void delete(Integer id) throws ValidationException {
        validatePrimary(id);

        if (deleteImpl(id) == 0) {
            throw new RecordNotFoundException("Record by id=" + id + " not found and image can't be deleted");
        }
    }

    public Image fetchByPrimary(Integer id) throws ValidationException {
        validatePrimary(id);

        return fetchByPrimaryImpl(id);
    }

    private void validatePrimary(Integer id) throws IncorrectPrimaryKeyException {
        ValidationUtils.checkPrimary(id);
    }

    private void validateName(String name) throws ValidationException {
        if (name == null || name.isEmpty()) {
            throw new EmptyFieldException("Field photo name must not be empty.");
        }

        int nameLimit = getNameLimit();
        if (name.length() > nameLimit) {
            throw new ToLongFieldException("Field photo name is too long. Max size of filed is " + nameLimit, nameLimit);
        }
    }

    private void validateComment(String comment) throws ValidationException {
        if (comment != null) {
            int commentLimit = getCommentLimit();
            if (comment.length() > commentLimit) {
                throw new ToLongFieldException("Field photo comment is too long. Max size of filed is " + commentLimit, commentLimit);
            }
        }
    }
}
