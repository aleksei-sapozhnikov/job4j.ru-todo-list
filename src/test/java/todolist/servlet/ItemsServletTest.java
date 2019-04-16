package todolist.servlet;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import todolist.constants.ContextAttrs;
import todolist.model.FrontItem;
import todolist.model.Item;
import todolist.model.Mapper;
import todolist.model.User;
import todolist.persistence.ItemDbStorage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemsServletTest {

    private final ItemDbStorage itemStorage = mock(ItemDbStorage.class);
    private final Mapper mapper = mock(Mapper.class);
    private final User user = mock(User.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpSession httpSession = mock(HttpSession.class);
    private final ServletContext context = mock(ServletContext.class);

    private final Gson jsonParser = new Gson();

    private final ItemsServlet testServlet = new ItemsServlet() {
        @Override
        public ServletContext getServletContext() {
            return ItemsServletTest.this.context;
        }
    };

    private Item createItem(long id, String description, long created, boolean done, User user) {
        var item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setCreated(created);
        item.setDone(done);
        item.setUser(user);
        return item;
    }

    @Before
    public void initMocks() {
        when(this.context.getAttribute(ContextAttrs.ITEM_STORAGE.v())).thenReturn(this.itemStorage);
        when(this.context.getAttribute(ContextAttrs.MAPPER.v())).thenReturn(this.mapper);
        when(this.context.getAttribute(ContextAttrs.JSON_PARSER.v())).thenReturn(this.jsonParser);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute(ContextAttrs.LOGGED_USER.v())).thenReturn(this.user);
    }

    @Test
    public void whenInitThenStorageTakenFromServletContext() {
        this.testServlet.init();
        Mockito.verify(this.context).getAttribute(ContextAttrs.ITEM_STORAGE.v());
        Mockito.verify(this.context).getAttribute(ContextAttrs.JSON_PARSER.v());
        Mockito.verify(this.context).getAttribute(ContextAttrs.MAPPER.v());

    }

    @Test
    public void whenDoGetThenReturnsJsonItemsForThisUser() throws IOException {
        this.testServlet.init();
        var fromStorage = List.of(
                this.createItem(1, "item1", 123L, true, this.user),
                this.createItem(2, "item2", 456L, false, this.user));
        var toFront = List.of(
                new FrontItem(1, "item1", 123L, true),
                new FrontItem(1, "item1", 123L, true)
        );
        when(this.user.getLogin()).thenReturn("test user");
        when(this.itemStorage.getForUser("test user")).thenReturn(fromStorage);
        when(this.mapper.itemToFrontItem(fromStorage)).thenReturn(toFront);
        String result;
        try (var out = new ByteArrayOutputStream();
             var writer = new PrintWriter(out)) {
            when(this.response.getWriter()).thenReturn(writer);
            this.testServlet.doGet(this.request, this.response);
            result = out.toString();
        }
        var expected = this.jsonParser.toJson(toFront);
        assertEquals(expected, result);
    }

    @Test
    public void whenDoPostThenMergesItemToStorageAndReturnsJsonFrontItemAsJsonObject() throws IOException {
        this.testServlet.init();
        var input = new FrontItem(0, "test item1", 123L, true);
        var toMerge = this.createItem(0, "test item1", 123L, true, this.user);
        var afterMerge = this.createItem(1, "test item1", 123L, true, this.user);
        var output = new FrontItem(1, "test item1", 123L, true);
        when(this.mapper.frontItemToItem(input, this.user)).thenReturn(toMerge);
        when(this.itemStorage.merge(toMerge)).thenReturn(afterMerge);
        when(this.mapper.itemToFrontItem(afterMerge)).thenReturn(output);
        String result;
        try (var in = new ByteArrayInputStream(this.jsonParser.toJson(input).getBytes());
             var reader = new BufferedReader(new InputStreamReader(in));
             var out = new ByteArrayOutputStream();
             var writer = new PrintWriter(out)
        ) {
            when(this.request.getReader()).thenReturn(reader);
            when(this.response.getWriter()).thenReturn(writer);
            this.testServlet.doPost(this.request, this.response);
            result = out.toString();
        }
        var expected = this.jsonParser.toJson(output);
        assertEquals(expected, result);
    }
}