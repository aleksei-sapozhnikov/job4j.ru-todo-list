package todolist.servlet;

import com.google.gson.Gson;
import todolist.model.Item;
import todolist.persistence.ItemDatabaseStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

<<<<<<<HEAD
=======
        >>>>>>>development
        <<<<<<<HEAD
=======
        >>>>>>>development

/**
 * Servlet working with items.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemsServlet extends HttpServlet {
<<<<<<<HEAD
    private final List<Item> storage = new ArrayList<>();
=======
    private static final int RESP_CODE_CREATED = 201;
    private final ItemDatabaseStorage storage = new ItemDatabaseStorage();
>>>>>>>development

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var writer = resp.getWriter()) {
<<<<<<<HEAD
            new Gson().toJson(this.storage, writer);
=======
            new Gson().toJson(this.storage.getAll(), writer);
>>>>>>>development
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
<<<<<<<HEAD
        Item item;
        try (var reader = req.getReader()) {
            item = new Gson().fromJson(reader, Item.class);
        }
        this.storage.add(item);
        resp.setStatus(201);
=======
        try (var reader = req.getReader();
             var writer = resp.getWriter()
        ) {
            var gson = new Gson();
            var item = gson.fromJson(reader, Item.class);
            item = this.storage.merge(item);
            gson.toJson(item, writer);
        }
        resp.setStatus(RESP_CODE_CREATED);
>>>>>>>development
    }
}
