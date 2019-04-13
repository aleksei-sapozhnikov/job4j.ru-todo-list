package todolist.servlet;

import com.google.gson.Gson;
import todolist.constants.ContextAttrs;
import todolist.constants.HttpCodes;
import todolist.model.Item;
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
        try (var writer = resp.getWriter()) {
            new Gson().toJson(this.itemStorage.getAll(), writer);
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
            var gson = new Gson();
            Item item = gson.fromJson(reader, Item.class);
            item = this.itemStorage.merge(item);
            gson.toJson(item, writer);
        }
        resp.setStatus(HttpCodes.CREATED.v());
    }
}
