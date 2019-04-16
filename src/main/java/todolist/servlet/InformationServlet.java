package todolist.servlet;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import todolist.constants.ContextAttrs;
import todolist.model.FrontInfo;
import todolist.model.User;

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
    /**
     * Json parser.
     */
    private Gson jsonParser;

    /**
     * Inits servlet-used objects.
     */
    @Override
    public void init() {
        var ctx = this.getServletContext();
        this.jsonParser = (Gson) ctx.getAttribute(ContextAttrs.JSON_PARSER.v());
    }

    /**
     * Returns JSON object with information stored in the current http session.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @throws IOException In case of problems.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var session = req.getSession();
        var user = (User) session.getAttribute(ContextAttrs.LOGGED_USER.v());
        var message = (String) session.getAttribute(ContextAttrs.MESSAGE.v());
        session.setAttribute(ContextAttrs.MESSAGE.v(), "");
        var info = new FrontInfo(user.getLogin(), message);
        try (var writer = resp.getWriter()) {
            this.jsonParser.toJson(info, writer);
        }
    }
}
