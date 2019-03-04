package todolist.servlet;

import com.google.gson.Gson;
import todolist.model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet working with items.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var stubItems = List.of(
                new Item(1, "s_one", System.currentTimeMillis(), true),
                new Item(2, "s_two", System.currentTimeMillis(), false),
                new Item(3, "s_three", System.currentTimeMillis(), false),
                new Item(4, "s_four", System.currentTimeMillis(), true),
                new Item(5, "s_five", System.currentTimeMillis(), true)
        );
        try (var writer = resp.getWriter()) {
            var gson = new Gson();
            gson.toJson(stubItems, writer);
        }
    }
}
