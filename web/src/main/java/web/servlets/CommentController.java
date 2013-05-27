package web.servlets;

import galleryService.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import persistence.exception.ValidationException;
import persistence.struct.User;
import web.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/20/13
 * Time: 5:35 PM
 */
@Controller
@RequestMapping("/Comment")
public class CommentController {

    public static final String IMAGE_ID = "imageId";

    private CommentService commentService;

    @Required
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(method = GET, value = "/{id}")
    public String getImageAndCommentFeed(Model model, @PathVariable int id, HttpServletRequest in) throws IOException, ValidationException {
        overrideOrAddImageIdToSession(in, id);

        addComments(model, id);
        model.addAttribute("id", id);
        return "image";
    }

    private void overrideOrAddImageIdToSession(HttpServletRequest in, int id) {
        SessionUtils.addAttribute(in, IMAGE_ID, id);
    }

    private int getImageIdFromSession(HttpServletRequest in) {
        return (int) SessionUtils.getAttribute(in, IMAGE_ID);
    }

    private User getUserFromSession(HttpServletRequest in) {
        return (User) SessionUtils.getAttribute(in, LoginController.USER);
    }

    @RequestMapping(method = POST)
    public
    @ResponseBody
    String addCommentToImage(Model model,
                             @RequestParam String comment,
                             HttpServletRequest in) throws ValidationException {
        int id = getImageIdFromSession(in);
        User user = getUserFromSession(in);

        commentService.addComment(user, comment, id);

        //  model.addAttribute("id", id);
        return "";
    }

    @RequestMapping(method = GET, value = "/all/{id}")
    public String getAllComments(Model model, @PathVariable int id, @RequestParam String date) throws ValidationException {
        System.out.println("DATE IS " + date);
        addComments(model, id);
        return "list";
    }

    private void addComments(Model model, int id) throws ValidationException {
        model.addAttribute("comments", commentService.getAllComments(id));
    }
}
