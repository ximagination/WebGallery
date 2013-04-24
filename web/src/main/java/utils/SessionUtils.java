package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/23/13
 * Time: 12:19 PM
 */
public class SessionUtils {

    private SessionUtils() {
        // not visible
    }

    public static void addAttribute(HttpServletRequest in, String key, Object attribute) {
        in.getSession().setAttribute(key, attribute);
    }

    public static boolean isAttributePresent(HttpServletRequest in, String attribute) {
        HttpSession session = in.getSession(false);

        if (session == null) {
            return false;
        }

        return session.getAttribute(attribute) != null;
    }

    public static void removeAttribute(HttpServletRequest in, String attribute) {
        HttpSession session = in.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(attribute);
    }
}
