package todolist.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import todolist.constants.ContextAttrs;
import todolist.model.User;
import todolist.persistence.UserDbStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login servlet.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class LoginServlet extends HttpServlet {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(LoginServlet.class);
    /**
     * Web-app root address.
     */
    private String baseDir;
    /**
     * User storage.
     */
    private UserDbStorage userStorage;

    /**
     * Inits servlet-used objects.
     */
    @Override
    public void init() {
        var ctx = this.getServletContext();
        this.baseDir = (String) this.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v());
        this.userStorage = (UserDbStorage) ctx.getAttribute(ContextAttrs.USER_STORAGE.v());
    }

    /**
     * Forwards to login page view.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @throws ServletException In case of problems.
     * @throws IOException      If problems occur.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(ContextAttrs.VIEW_LOGIN.v()).forward(req, resp);
    }

    /**
     * Checks given login and passwords and returns result.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @throws ServletException In case of problems.
     * @throws IOException      If problems occur.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var login = req.getParameter(ContextAttrs.PRM_LOGIN.v());
        var password = req.getParameter(ContextAttrs.PRM_PASSWORD.v());
        var user = this.userStorage.getOrAdd(new User().setLogin(login).setPassword(password));
        if (user.getPassword().equals(password)) {
            this.attachAndPass(req, resp, user);
        } else {
            this.redirectWithError(resp);
        }
    }

    /**
     * Redirects to login page with error message.
     *
     * @param resp Response object.
     * @throws ServletException In case of problems.
     * @throws IOException      If problems occur.
     */
    private void redirectWithError(HttpServletResponse resp) throws IOException, ServletException {
        var msg = "Error: wrong password";
        resp.sendRedirect(new StringBuilder()
                .append(this.baseDir)
                .append(ContextAttrs.ADDR_LOGIN.v())
                .append("?").append(ContextAttrs.MESSAGE.v()).append("=").append(msg)
                .toString()
        );
    }

    /**
     * Attaches user to session and redirects to main page.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @param user User to attach.
     * @throws IOException In case of problems.
     */
    private void attachAndPass(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        var msg = String.format("User logged in: %s", user.getLogin());
        var session = req.getSession();
        session.setAttribute(ContextAttrs.LOGGED_USER.v(), user);
        session.setAttribute(ContextAttrs.MESSAGE.v(), msg);
        resp.sendRedirect(this.baseDir);
    }
}
