package web.servlets;

import galleryService.ServiceHolder;
import galleryService.services.ImageService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import persistence.exception.ValidationException;
import persistence.struct.User;
import web.utils.JSPUtils;
import web.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/25/13
 * Time: 1:38 PM
 */
@MultipartConfig
public class ImageUpload extends HttpServlet {

    //Session params
    public static final String UPLOAD_MESSAGE = "upload_message";

    //Action
    public static final String UPLOAD = "upload";

    //Fields
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String PATH = "path";

    //Logger
    private static final Logger LOGGER = Logger.getLogger(ImageUpload.class);

    protected void doGet(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        /*
         Ignore.
         */
        JSPUtils.showHomePage(out);
    }

    protected void doPost(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        /*
        Always remove upload error message
        */
        SessionUtils.removeAttribute(in, UPLOAD_MESSAGE);

        byte[] data = loadFile(in);

        if (data != null) {
            saveImage(in, out, data);

        } else {
            onFail(in, out, "can't upload file. Some error occurs.");
        }
    }

    private void saveImage(HttpServletRequest in, HttpServletResponse out, byte[] data) throws IOException {
        User user = getUser(in);

        String name = in.getParameter(NAME);
        String comment = in.getParameter(COMMENT);

        ImageService imageService = ServiceHolder.getImageService();

        try {
            imageService.addImage(user, name, comment, data);

            onSuccess(in, out);
        } catch (ValidationException e) {
            onFail(in, out, e.getMessage());

        } catch (IOException e) {
            onFail(in, out, e.getMessage());

        } catch (RuntimeException e) {
            onFail(in, out, e.getMessage());
        }
    }

    private User getUser(HttpServletRequest in) {
        return (User) SessionUtils.getAttribute(in, LogIn.USER);
    }

    private byte[] loadFile(HttpServletRequest in) throws ServletException {
        InputStream stream = null;
        byte[] image = null;

        try {
            Part filePart = in.getPart(PATH);

            if (filePart != null) {
                stream = filePart.getInputStream();
                image = IOUtils.toByteArray(stream);
            }

        } catch (IOException e) {
            LOGGER.warn("Image can't be uploaded. ", e);

        } finally {
            IOUtils.closeQuietly(stream);
        }

        return image;
    }

    private void onSuccess(HttpServletRequest in, HttpServletResponse out) throws IOException {
        addMessage(in, "File uploaded");

        JSPUtils.showHomePage(out);
    }

    private void onFail(HttpServletRequest in, HttpServletResponse out, String message) throws IOException {
        addMessage(in, "Upload failed because " + message);

        JSPUtils.showHomePage(out);
    }

    private void addMessage(HttpServletRequest in, String message) {
        SessionUtils.addAttribute(in, UPLOAD_MESSAGE, message);
    }
}
