package web.servlets;

import galleryService.interfaces.ImageService;
import galleryService.pojo.ImageInfoHolder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import persistence.exception.ValidationException;
import persistence.struct.Image;
import web.pojo.Login;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/20/13
 * Time: 5:35 PM
 */
@Controller
@RequestMapping("/Gallery")
public class GalleryController {

    private static final Image[][] EMPTY = new Image[0][0];

    @Value(value = "${web.gallery.imageCountInRow}")
    private int imageCountInRow;

    @Value(value = "${web.gallery.imageCountInPage}")
    private int imageCountInPage;

    private ImageService imageService;

    @Required
    public void setAutentificationService(ImageService imageService) {
        this.imageService = imageService;
    }

    public enum ImageType {
        ORIGINAL, PREVIEW
    }

    @RequestMapping(method = GET, value = "/{id}")
    public void getImage(@PathVariable int id,
                         @RequestParam(value = "type", required = false, defaultValue = "ORIGINAL")
                         ImageType type,
                         HttpServletResponse response) throws IOException, ValidationException {
        InputStream is = null;
        try {
            is = imageService.getImageById(id, type == ImageType.ORIGINAL);
            IOUtils.copy(is, response.getOutputStream());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @RequestMapping(method = {GET, POST})
    public String getPage(Model model,
                          @RequestParam(value = "pageId", required = false, defaultValue = "0")
                          int pageId) throws ValidationException {
        ImageInfoHolder imageInfo = imageService.getImages(pageId * imageCountInPage, imageCountInPage);
        model.addAttribute("login", new Login());
        model.addAttribute("images", split(imageInfo.getList(), imageCountInRow));
        model.addAttribute("countOfPages", Math.ceil(imageInfo.getCount() / (float) imageCountInPage));

        return "gallery";
    }

    private Image[][] split(List<Image> list, int countInRow) {
        if (list == null || list.isEmpty()) {
            return EMPTY;
        }

        int countOfRows = (int) Math.ceil(list.size() / (float) countInRow);
        Image[][] images = new Image[countOfRows][countInRow];

        int mRow = 0;
        int mIndex = 0;
        int limitCount = countInRow - 1;

        for (int index = 0; index < list.size(); index++) {
            Image value = list.get(index);

            if (mIndex > limitCount) {
                mRow++;
                mIndex = 0;
            }

            images[mRow][mIndex] = value;
            mIndex++;
        }

        return images;
    }
}
