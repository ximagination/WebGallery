package persistence.dao.interfaces;

import persistence.exception.ValidationException;
import persistence.struct.Image;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/22/13
 * Time: 6:07 PM
 */
public interface ImageDAO extends BaseDAO<Image, Integer> {

    int getCount();

    List<Image> fetchWithOffset(int offset, int limit) throws ValidationException;

}
