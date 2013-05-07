package web.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import web.utils.JSPUtils;
import web.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/22/13
 * Time: 12:57 PM
 */
@Controller
@RequestMapping("/LogOut")
public class LogOut {

    @RequestMapping(method = RequestMethod.POST)
    protected void doPost(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        action(in, out);
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        action(in, out);
    }

    private void action(HttpServletRequest in, HttpServletResponse out) throws IOException {
        SessionUtils.clearAllAttributes(in);
        JSPUtils.showHomePage(out);
    }
}

