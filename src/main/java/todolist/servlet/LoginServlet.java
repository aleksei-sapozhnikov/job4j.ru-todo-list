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

    private UserDbStorage userStorage;

    @Override
    public void init() throws ServletException {
        var ctx = this.getServletContext();
        this.userStorage = (UserDbStorage) ctx.getAttribute(ContextAttrs.USER_STORAGE.v());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var login = req.getParameter("login");
        var password = req.getParameter("password");
        var msg = String.format("success=user-%s-logged-in", login);
        var user = this.userStorage.getByLogin(login);
        if (user == null) {
            user = new User().setLogin(login).setPassword(password);
            user = this.userStorage.merge(user);
            msg = String.format("success=user-%s-created", login);
        }
        if (user.getPassword().equals(password)) {
            req.getSession().setAttribute("loggedUserId", user.getId());
            req.getSession().setAttribute("loggedUserLogin", user.getLogin());
            resp.sendRedirect(req.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v()) + "index.html" + "?" + msg);
        } else {
            msg = "error=wrong-password";
            resp.sendRedirect(req.getServletContext().getAttribute(ContextAttrs.BASE_DIR.v()) + "login.html" + "?" + msg);
        }
    }
}
