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

    private String baseDir;

    private UserDbStorage userStorage;


    @Override
    public void init() throws ServletException {
        var ctx = this.getServletContext();
        this.baseDir = (String) this.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v());
        this.userStorage = (UserDbStorage) ctx.getAttribute(ContextAttrs.USER_STORAGE.v());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var login = req.getParameter(ContextAttrs.PRM_LOGIN.v());
        var password = req.getParameter(ContextAttrs.PRM_PASSWORD.v());
        var user = this.userStorage.getOrAdd(new User().setLogin(login).setPassword(password));
        if (user.getPassword().equals(password)) {
            this.attachAndPass(req, resp, login, user);
        } else {
            this.redirectWithError(resp);
        }
    }

    private void redirectWithError(HttpServletResponse resp) throws IOException, ServletException {
        var msg = "Error: wrong password";
        resp.sendRedirect(new StringBuilder()
                .append(this.baseDir)
                .append(ContextAttrs.PAGE_LOGIN.v())
                .append("?").append(ContextAttrs.MESSAGE.v()).append("=").append(msg)
                .toString()
        );
    }

    private void attachAndPass(HttpServletRequest req, HttpServletResponse resp, String login, User user) throws IOException {
        var msg = String.format("User logged in: %s", login);
        var session = req.getSession();
        session.setAttribute(ContextAttrs.LOGGED_USER.v(), user);
        session.setAttribute(ContextAttrs.MESSAGE.v(), msg);
        resp.sendRedirect(String.format("%s",
                req.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v())
        ));
    }
}
