package todolist.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter setting Character encoding to response.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class CharacterEncodingUtf8Filter implements Filter {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(CharacterEncodingUtf8Filter.class);

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
     * @param servletRequest  Request object.
     * @param servletResponse Response object.
     * @param filterChain     Filters on this resource.
     * @throws IOException      In case of filtering problems.
     * @throws ServletException In case of filtering problems.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Method on filter destroy.
     * Unused.
     */
    @Override
    public void destroy() {

    }
}
