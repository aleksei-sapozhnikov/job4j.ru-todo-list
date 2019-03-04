package todolist.servlet;

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
    private static final Logger LOG = LogManager.getLogger(ContentTypeTextHtmlFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/html");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
