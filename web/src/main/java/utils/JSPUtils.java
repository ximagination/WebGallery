package utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/23/13
 * Time: 1:20 PM
 */
public class JSPUtils {

    private static final String DEFAULT_PAGE = "/LogIn.jsp";

    private JSPUtils() {
        // not visible
    }

    public static void showHomePage(HttpServletResponse out) throws IOException {
        out.sendRedirect(DEFAULT_PAGE);
    }
}
