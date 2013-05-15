package web.filters;

import web.utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 4/26/13
 * Time: 1:40 PM
 */
public class AuthenticationFilter implements Filter {

    private static final String CSS = ".css";

    // settings
    private String defaultPage;
    private String[] publicPages;

    public void destroy() {
        defaultPage = null;
        publicPages = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (isValid(req)) {
            chain.doFilter(req, resp);
        } else {
            authPage(resp);
        }
    }

    private boolean isValid(HttpServletRequest req) {
        String path = req.getRequestURI();

        if (isDefaultPage(path)
                || isCss(path)
                || isPublicPage(path)
                || isAuthenticated(req)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAuthenticated(HttpServletRequest req) {
        return SessionUtils.isUserAuthenticated(req);
    }

    private boolean isDefaultPage(String path) {
        return path.equals(defaultPage);
    }

    private boolean isCss(String path) {
        return path.endsWith(CSS);
    }

    private void authPage(HttpServletResponse req) throws IOException {
        req.sendRedirect("/");
    }

    private boolean isPublicPage(String path) {
        for (String each : publicPages) {
            if (each.trim().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public void init(FilterConfig config) throws ServletException {
        this.defaultPage = config.getInitParameter("default_page");
        this.publicPages = config.getInitParameter("public_pages").split(",");
    }
}
