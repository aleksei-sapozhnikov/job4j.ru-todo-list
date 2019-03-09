package todolist.servlet;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import todolist.Constants;
import todolist.model.Item;
import todolist.persistence.ItemStorage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemsServletTest {

    private final ItemStorage storage = mock(ItemStorage.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final ServletContext context = mock(ServletContext.class);

    private final ItemsServlet testServlet = new ItemsServlet() {
        @Override
        public ServletContext getServletContext() {
            return ItemsServletTest.this.context;
        }
    };

    private Item createItem(int id, String description, long created, boolean done) {
        var item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setCreated(created);
        item.setDone(done);
        return item;
    }

    @Before
    public void initContext() {
        when(this.context
                .getAttribute(Constants.CONTEXT_ATTR_STORAGE.getValue()))
                .thenReturn(this.storage);
    }

    @Test
    public void whenInitThenStorageTakenFromServletContext() {
        this.testServlet.init();
        Mockito.verify(this.context)
                .getAttribute(Constants.CONTEXT_ATTR_STORAGE.getValue());
    }

    @Test
    public void whenDoGetThenReturnsJsonStringOfAllObjectsFromStorage() throws IOException {
        this.testServlet.init();
        var fromStorage = List.of(
                this.createItem(1, "item1", 123L, true),
                this.createItem(2, "item2", 456L, false));
        when(this.storage.getAll()).thenReturn(fromStorage);
        String result;
        try (var out = new ByteArrayOutputStream();
             var writer = new PrintWriter(out)) {
            when(this.response.getWriter()).thenReturn(writer);
            this.testServlet.doGet(this.request, this.response);
            result = out.toString();
        }
        var expected = new Gson().toJson(fromStorage);
        assertEquals(expected, result);
    }

    @Test
    public void whenDoPostThenMergesItemToStorageAndReturnsResultItemAsJsonObject() throws IOException {
        this.testServlet.init();
        var gson = new Gson();
        var added = this.createItem(1, "item1", 123L, false);
        var addedJson = gson.toJson(added);
        var returned = this.createItem(2, "result", 678L, true);
        String result;
        try (var in = new ByteArrayInputStream(addedJson.getBytes());
             var reader = new BufferedReader(new InputStreamReader(in));
             var out = new ByteArrayOutputStream();
             var writer = new PrintWriter(out)
        ) {
            when(this.request.getReader()).thenReturn(reader);
            when(this.response.getWriter()).thenReturn(writer);
            when(this.storage.merge(added)).thenReturn(returned);
            this.testServlet.doPost(this.request, this.response);
            result = out.toString();
        }
        var expected = gson.toJson(returned);
        assertEquals(expected, result);
    }
}