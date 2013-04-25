package servlets;

import galleryService.ServiceHolder;
import galleryService.services.AutentificationService;
import persistence.exception.ValidationException;
import persistence.struct.User;
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
public class LogIn extends HttpServlet {

    //Session params
    public static final String USER = "user";
    public static final String WARNING = "warning";

    //Actions
    public static final String AUTENTIFICATION = "autentification";
    public static final String REGISTRATION = "registration";

    //Fields
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

    protected void doPost(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        identificationOrRegistration(in, out);
    }

    protected void doGet(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {
        identificationOrRegistration(in, out);
    }

    private void identificationOrRegistration(HttpServletRequest in, HttpServletResponse out) throws ServletException, IOException {

        // always remove old warning
        SessionUtils.removeAttribute(in, WARNING);

        if (SessionUtils.isAttributePresent(in, USER)) {
            success(out);

        } else {

            if (isAutentification(in)) {
                restoreUserInSession(in, out, AUTENTIFICATION);

            } else if (isRegistration(in)) {
                restoreUserInSession(in, out, REGISTRATION);

            } else {
                JSPUtils.showHomePage(out);
            }
        }
    }

    private void restoreUserInSession(HttpServletRequest in, HttpServletResponse out, String type) throws IOException {

        String login = getLogin(in);
        String password = getPassword(in);

        try {

            AutentificationService service = ServiceHolder.getAutentificationService();

            if (AUTENTIFICATION.equals(type)) {
                User user = service.autentificate(login, password);
                addToSessionAndRedirect(in, out, user);

            } else if (REGISTRATION.equals(type)) {
                User user = service.register(login, password);
                addToSessionAndRedirect(in, out, user);

            } else {
                /*
                 if user get access to this class like this  ../LogIn
                 redirect him to login page without error message
                 */
                logInPage(out);
            }

        } catch (ValidationException e) {
            errorOutput(in, out, e);

        } catch (RuntimeException e) {
            errorOutput(in, out, e);
        }
    }

    private void addToSessionAndRedirect(HttpServletRequest in, HttpServletResponse out, User user) throws IOException {
        SessionUtils.addAttribute(in, USER, user);

        success(out);
    }

    private void errorOutput(HttpServletRequest in, HttpServletResponse out, Throwable e) throws IOException {
        SessionUtils.addAttribute(in, WARNING, e.getMessage());

        logInPage(out);
    }

    private void logInPage(HttpServletResponse out) throws IOException {
        out.sendRedirect("/LogIn.jsp");
    }

    private void success(HttpServletResponse out) throws IOException {
        JSPUtils.showHomePage(out);
    }

    private boolean isAutentification(HttpServletRequest in) {
        return in.getParameterMap().containsKey(AUTENTIFICATION);
    }

    private boolean isRegistration(HttpServletRequest in) {
        return in.getParameterMap().containsKey(REGISTRATION);
    }

    private String getLogin(HttpServletRequest in) {
        return in.getParameter(LOGIN);
    }

    private String getPassword(HttpServletRequest in) {
        return in.getParameter(PASSWORD);
    }
}
