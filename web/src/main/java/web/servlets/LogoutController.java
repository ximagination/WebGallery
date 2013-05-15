package web.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import web.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/22/13
 * Time: 12:57 PM
 */
@Controller
@RequestMapping("/Logout")
public class LogoutController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String action(HttpServletRequest in) {
        SessionUtils.clearAllAttributes(in);
        return "redirect:/Login";
    }
}

