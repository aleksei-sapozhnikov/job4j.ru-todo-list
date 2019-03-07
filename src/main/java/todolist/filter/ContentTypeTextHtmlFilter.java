package todolist.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter setting content type to response.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ContentTypeTextHtmlFilter implements Filter {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(ContentTypeTextHtmlFilter.class);

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
     * Sets Character encoding to all response objects passing through.
     *
     * @param request  Request object.
     * @param response Response object.
     * @param chain    Filters on this resource.
     * @throws IOException      In case of filtering problems.
     * @throws ServletException In case of filtering problems.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/html");
        chain.doFilter(request, response);
    }

    /**
     * Method on filter destroy.
     * Unused.
     */
    @Override
    public void destroy() {

    }
}
