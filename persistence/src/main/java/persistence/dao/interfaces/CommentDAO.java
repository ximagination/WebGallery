package persistence.dao.interfaces;

import persistence.exception.ValidationException;
import persistence.struct.Comment;

import java.util.List;

public interface CommentDAO extends BaseDAO<Comment, Integer> {

    List<Comment> getCommentsForImage(int id) throws ValidationException;

}