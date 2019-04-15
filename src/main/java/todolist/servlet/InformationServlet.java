package todolist.servlet;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import todolist.constants.ContextAttrs;
import todolist.model.FrontInfo;
import todolist.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns session info.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class InformationServlet extends HttpServlet {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(InformationServlet.class);

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession();
        var user = (User) session.getAttribute(ContextAttrs.LOGGED_USER.v());
        var message = (String) session.getAttribute(ContextAttrs.MESSAGE.v());
        session.setAttribute(ContextAttrs.MESSAGE.v(), "");
        var info = new FrontInfo(user.getLogin(), message);
        try (var writer = resp.getWriter()) {
            this.gson.toJson(info, writer);
        }
    }
}
