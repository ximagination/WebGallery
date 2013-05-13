package web.utils;

import web.servlets.LoginController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

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

    public static boolean isUserAuthenticated(HttpServletRequest in) {
        return SessionUtils.isAttributePresent(in, LoginController.USER);
    }

    public static void addAttribute(HttpServletRequest in, String key, Object attribute) {
        in.getSession().setAttribute(key, attribute);
    }

    public static Object getAttribute(HttpServletRequest in, String key) {
        HttpSession session = in.getSession(false);

        Object result = null;
        if (session != null) {
            result = session.getAttribute(key);
        }

        return result;
    }

    public static boolean isAttributePresent(HttpServletRequest in, String attribute) {
        HttpSession session = in.getSession(false);

        boolean attrPresent = false;
        if (session != null) {
            attrPresent = session.getAttribute(attribute) != null;
        }

        return attrPresent;
    }

    public static void removeAttribute(HttpServletRequest in, String attribute) {
        HttpSession session = in.getSession(false);

        if (session != null) {
            session.removeAttribute(attribute);
        }
    }

    public static void clearAllAttributes(HttpServletRequest in) {
        HttpSession session = in.getSession(false);

        if (session != null) {
            Enumeration<String> names = session.getAttributeNames();

            while (names.hasMoreElements()) {
                session.removeAttribute(names.nextElement());
            }
        }
    }
}
