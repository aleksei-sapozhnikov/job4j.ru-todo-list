package todolist.servlet;

import com.google.gson.Gson;
import todolist.model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet working with items.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemsServlet extends HttpServlet {
    private final List<Item> storage = new ArrayList<>();

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
            System.out.println("Trying to read object");
            item = new Gson().fromJson(reader, Item.class);
        }
        this.storage.add(item);
    }
}
