package todolist.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import todolist.constants.ContextAttrs;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout servlet.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class LogoutServlet extends HttpServlet {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(LogoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        var msg = "Logged out";
        resp.sendRedirect(new StringBuilder()
                .append(req.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v()))
                .append(ContextAttrs.PAGE_LOGIN.v())
                .append("?").append(ContextAttrs.MESSAGE.v()).append("=").append(msg)
                .toString()
        );
    }
}
