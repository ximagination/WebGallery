package web.servlets;

import galleryService.interfaces.AutentificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import persistence.exception.ValidationException;
import persistence.struct.User;
import web.pojo.Login;
import web.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static web.servlets.ImageUploadController.UPLOAD_MESSAGE;
import static web.utils.JSPUtils.DEFAULT_PAGE;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/22/13
 * Time: 12:57 PM
 */
@Controller
@RequestMapping("/Login")
public class LoginController {

    //Session params
    public static final String USER = "user";
    public static final String WARNING = "warning";

    //Actions
    public static final String AUTENTIFICATION = "autentification";
    public static final String REGISTRATION = "registration";

    //Fields
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

    //Logger
    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    private AutentificationService autentificationService;

    @Required
    public void setAutentificationService(AutentificationService autentificationService) {
        this.autentificationService = autentificationService;
    }

    @RequestMapping(method = GET)
    public String handleRequest(Model model) {
        model.addAttribute("login", new Login());
        return DEFAULT_PAGE;
    }

    @RequestMapping(method = POST)
    public String identificationOrRegistration(
            HttpServletRequest in,

            @Valid
            Login login, BindingResult binding) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(login.toString());

            boolean hasErrors = binding.hasErrors();

            LOGGER.debug("BindingResult has errors? " + hasErrors);
            if (hasErrors) {
                LOGGER.debug("======== Error list ==========");
                for (ObjectError each : binding.getAllErrors()) {
                    LOGGER.debug(each.toString());
                }
            }
        }

        // always remove old warnings and notifications
        SessionUtils.removeAttribute(in, WARNING);
        SessionUtils.removeAttribute(in, UPLOAD_MESSAGE);

        if (!binding.hasErrors()) {
            if (isAutentification(in)) {
                restoreUserInSession(in, AUTENTIFICATION);

            } else if (isRegistration(in)) {
                restoreUserInSession(in, REGISTRATION);
            }
        }

        return DEFAULT_PAGE;
    }

    private void restoreUserInSession(HttpServletRequest in, String type) {

        String login = getLogin(in);
        String password = getPassword(in);

        try {

            AutentificationService service = getAutentificationService();

            if (AUTENTIFICATION.equals(type)) {
                User user = service.autentificate(login, password);
                addToSession(in, user);

            } else if (REGISTRATION.equals(type)) {
                User user = service.register(login, password);
                addToSession(in, user);
            }

        } catch (ValidationException e) {
            errorOutput(in, e);

        } catch (RuntimeException e) {
            LOGGER.error("Failed on auth user", e);

            errorOutput(in, e);
        }
    }

    private void addToSession(HttpServletRequest in, User user) {
        SessionUtils.addAttribute(in, USER, user);
    }

    private void errorOutput(HttpServletRequest in, Throwable e) {
        SessionUtils.addAttribute(in, WARNING, e.getMessage());
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

    public AutentificationService getAutentificationService() {
        return autentificationService;
    }
}
