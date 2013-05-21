package web.servlets;

import galleryService.interfaces.ImageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import persistence.exception.ValidationException;
import persistence.struct.User;
import web.pojo.Login;
import web.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static web.utils.JSPUtils.DEFAULT_PAGE;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/25/13
 * Time: 1:38 PM
 */
@Controller
@RequestMapping("/ImageUpload")
public class ImageUploadController {

    //Session params
    public static final String UPLOAD_MESSAGE = "upload_message";

    //Fields
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String PATH = "path";

    //Logger
    private static final Logger LOGGER = Logger.getLogger(ImageUploadController.class);

    private ImageService imageService;

    @Required
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(method = GET)
    public String doGet() {
        return "redirect:/Login";
    }

    @RequestMapping(method = POST)
    protected String doPost(HttpServletRequest in, Model model,
                            @RequestParam(value = PATH) MultipartFile file) {
        /*
        Always remove upload error message
        */
        SessionUtils.removeAttribute(in, UPLOAD_MESSAGE);

        byte[] data = loadFile(file);

        if (data != null) {
            saveImage(in, data);

        } else {
            onFail(in, "can't upload file. Some error occurs.");
        }

        model.addAttribute("login", new Login());
        return DEFAULT_PAGE;
    }

    private byte[] loadFile(MultipartFile file) {
        byte[] image = null;
        try {
            image = file.getBytes();
        } catch (IOException e) {
            LOGGER.warn("Image can't be uploaded. ", e);
        }
        return image;
    }

    private void saveImage(HttpServletRequest in, byte[] data) {
        User user = getUser(in);

        String name = in.getParameter(NAME);
        String comment = in.getParameter(COMMENT);

        ImageService imageService = getService();

        try {
            imageService.addImage(user, name, comment, data);

            onSuccess(in);
        } catch (ValidationException e) {
            onFail(in, e.getMessage());

        } catch (IOException e) {
            LOGGER.error("Failed on upload image. ", e);

            onFail(in, e.getMessage());

        } catch (RuntimeException e) {
            LOGGER.error("Failed on upload image. ", e);

            onFail(in, e.getMessage());
        }
    }

    private User getUser(HttpServletRequest in) {
        return (User) SessionUtils.getAttribute(in, LoginController.USER);
    }

    private void onSuccess(HttpServletRequest in) {
        addMessage(in, "File uploaded");
    }

    private void onFail(HttpServletRequest in, String message) {
        addMessage(in, "Upload failed because " + message);
    }

    private void addMessage(HttpServletRequest in, String message) {
        SessionUtils.addAttribute(in, UPLOAD_MESSAGE, message);
    }

    public ImageService getService() {
        return imageService;
    }
}
