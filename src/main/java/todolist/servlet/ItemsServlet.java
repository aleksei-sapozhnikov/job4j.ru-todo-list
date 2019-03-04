package todolist.servlet;

import com.google.gson.Gson;
import todolist.model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NavigableSet;
import java.util.Random;
import java.util.TreeSet;

/**
 * Servlet working with items.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemsServlet extends HttpServlet {
    private static final int RESP_CODE_CREATED = 201;
    private final NavigableSet<Item> storage = new TreeSet<>();
    private final Random random = new Random();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var writer = resp.getWriter()) {
            new Gson().toJson(this.storage, writer);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Item item;
        try (var reader = req.getReader()) {
            item = new Gson().fromJson(reader, Item.class);
        }
        if (item.getId() == -1) {
            item.setId(random.nextInt());
        }
        this.storage.remove(item);
        this.storage.add(item);
        resp.setStatus(RESP_CODE_CREATED);
    }
}
