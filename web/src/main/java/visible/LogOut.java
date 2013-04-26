package visible;

import utils.JSPUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/22/13
 * Time: 12:57 PM
 */
public class LogOut extends HttpServlet {

    protected void doPost(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        action(in, out);
    }

    protected void doGet(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        action(in, out);
    }

    private void action(HttpServletRequest in, HttpServletResponse out) throws IOException {
        SessionUtils.clearAllAttributes(in);
        JSPUtils.showHomePage(out);
    }
}

