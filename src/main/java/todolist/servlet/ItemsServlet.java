package todolist.servlet;

import com.google.gson.Gson;
import todolist.constants.ContextAttrs;
import todolist.constants.HttpCodes;
import todolist.model.FrontItem;
import todolist.model.Mapper;
import todolist.model.User;
import todolist.persistence.ItemDbStorage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet working with items.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemsServlet extends HttpServlet {
    /**
     * Storage of items.
     */
    private ItemDbStorage itemStorage;
    /**
     * JSON parser.
     */
    private final Gson gson = new Gson();

    private final Mapper mapper = new Mapper();

    /**
     * Inits servlet-used objects.
     */
    @Override
    public void init() {
        this.itemStorage = (ItemDbStorage) this.getServletContext()
                .getAttribute(ContextAttrs.ITEM_STORAGE.v());
    }

    /**
     * Returns list of currently stored items.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @throws IOException If problems getting response writer occur.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var session = req.getSession();
        var user = (User) session.getAttribute(ContextAttrs.LOGGED_USER.v());
        var login = user.getLogin();
        try (var writer = resp.getWriter()) {
            var items = this.mapper.itemToFrontItem(this.itemStorage.getForUser(login));
            this.gson.toJson(items, writer);
        }
    }

    /**
     * Takes item to add into storage or to update, gives it to storage
     * and returns storage response as Item object.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @throws IOException If problems getting request reader /  response writer occur.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (var reader = req.getReader();
             var writer = resp.getWriter()
        ) {
            var front = this.gson.fromJson(reader, FrontItem.class);
            var item = this.mapper.frontItemToItem(front, (User) req.getSession().getAttribute(ContextAttrs.LOGGED_USER.v()));
            item = this.itemStorage.merge(item);
            this.gson.toJson(this.mapper.itemToFrontItem(item), writer);
        }
        resp.setStatus(HttpCodes.CREATED.v());
    }
}
