package galleryService.pojo;

import persistence.struct.Image;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/20/13
 * Time: 11:23 AM
 */
public class ImageInfoHolder {

    private final int count;
    private final List<Image> list;

    public ImageInfoHolder(int count, List<Image> list) {
        this.count = count;
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public List<Image> getList() {
        return list;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ImageInfoHolder");
        sb.append("{count=").append(count);
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
