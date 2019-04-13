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


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var resp = (HttpServletResponse) response;
        var session = req.getSession();
        var uri = req.getRequestURI();
        var pass = (session != null && session.getAttribute("loggedUserId") != null)
                || (uri.endsWith("/login.html") || uri.endsWith("/login"));
        if (pass) {
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect(req.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v()) + "login.html");
        }
    }

    @Override
    public void destroy() {

    }
}
