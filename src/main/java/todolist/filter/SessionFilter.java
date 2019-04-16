package todolist.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import todolist.constants.ContextAttrs;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Checks if user is logged.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class SessionFilter implements Filter {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SessionFilter.class);

    /**
     * Init filter method.
     * Unused.
     *
     * @param filterConfig Filter config object.
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * Checks if session contains logged user.
     * If not - redirects to login page.
     *
     * @param request  Request object.
     * @param response Response object.
     * @param chain    Filters on this resource.
     * @throws IOException      In case of filtering problems.
     * @throws ServletException In case of filtering problems.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var resp = (HttpServletResponse) response;
        var session = req.getSession();
        var uri = req.getRequestURI();
        var pass = (session != null && session.getAttribute(ContextAttrs.LOGGED_USER.v()) != null)
                || uri.endsWith(ContextAttrs.ADDR_LOGIN.v());
        if (pass) {
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect(req.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v()) + ContextAttrs.ADDR_LOGIN.v());
        }
    }

    /**
     * Method on filter destroy.
     * Unused.
     */
    @Override
    public void destroy() {

    }
}
