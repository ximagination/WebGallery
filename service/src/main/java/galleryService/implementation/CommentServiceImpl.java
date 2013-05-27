package galleryService.implementation;

import galleryService.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import persistence.dao.interfaces.CommentDAO;
import persistence.exception.ValidationException;
import persistence.struct.Comment;
import persistence.struct.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 6:04 PM
 */
@Service
public class CommentServiceImpl implements CommentService {

    private CommentDAO commentDAO;

    @Required
    public void setCommentDAO(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }

    @Override
    public void addComment(User user, String text, int imageId) throws ValidationException {
        if (user == null) {
            throw new NullPointerException("User is null");
        }

        Comment comment = new Comment();
        comment.setImageId(imageId);
        comment.setUserId(user.getId());
        comment.setComment(text);

        commentDAO.insert(comment);
    }

    @Override
    public List<Comment> getAllComments(int imageId) throws ValidationException {
        return commentDAO.getCommentsForImage(imageId);
    }
}
