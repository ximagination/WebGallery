package galleryService.interfaces;

import persistence.exception.ValidationException;
import persistence.struct.Comment;
import persistence.struct.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/23/13
 * Time: 6:03 PM
 */
public interface CommentService {

    void addComment(User user, String comment, int imageId) throws ValidationException;

    List<Comment> getAllComments(int imageId) throws ValidationException;

}
