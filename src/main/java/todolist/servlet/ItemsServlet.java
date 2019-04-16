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
    private Gson jsonParser;
    /**
     * Converts objects one into another.
     */
    private Mapper mapper;

    /**
     * Inits servlet-used objects.
     */
    @Override
    public void init() {
        var ctx = this.getServletContext();
        this.itemStorage = (ItemDbStorage) ctx.getAttribute(ContextAttrs.ITEM_STORAGE.v());
        this.jsonParser = (Gson) ctx.getAttribute(ContextAttrs.JSON_PARSER.v());
        this.mapper = (Mapper) ctx.getAttribute(ContextAttrs.MAPPER.v());
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
            this.jsonParser.toJson(items, writer);
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
            var front = this.jsonParser.fromJson(reader, FrontItem.class);
            var user = (User) req.getSession().getAttribute(ContextAttrs.LOGGED_USER.v());
            var item = this.mapper.frontItemToItem(front, user);
            item = this.itemStorage.merge(item);
            this.jsonParser.toJson(this.mapper.itemToFrontItem(item), writer);
        }
        resp.setStatus(HttpCodes.CREATED.v());
    }
}
