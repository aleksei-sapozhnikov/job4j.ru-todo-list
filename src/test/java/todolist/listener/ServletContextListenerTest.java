package todolist.listener;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import todolist.constants.ContextAttrs;
import todolist.persistence.ItemDbStorage;
import todolist.persistence.UserDbStorage;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ServletContextListenerTest {

    private final ItemDbStorage storage = mock(ItemDbStorage.class);

    private final ServletContextEvent contextEvent = mock(ServletContextEvent.class);
    private final ServletContext context = mock(ServletContext.class);
    private final SessionFactory hbFactory = mock(SessionFactory.class);

    @Test
    public void whenContextInitializedThenStorageSetAsParameter() {
        when(this.contextEvent.getServletContext()).thenReturn(this.context);
        new ServletContextListener().contextInitialized(contextEvent);
        verify(this.context).setAttribute(eq(ContextAttrs.ITEM_STORAGE.v()), any(ItemDbStorage.class));
        verify(this.context).setAttribute(eq(ContextAttrs.USER_STORAGE.v()), any(UserDbStorage.class));
    }

    @Test
    public void whenContextDestroyedThenStorageClosed() {
        var listener = new ServletContextListener();
        Whitebox.setInternalState(listener, "hbFactory", this.hbFactory);
        listener.contextDestroyed(this.contextEvent);
        verify(this.hbFactory).close();
    }

    @Test
    public void whenStorageThrowsExceptionOnCloseThenRuntimeExceptionThrownWithSuppressed() {
        var listener = new ServletContextListener();
        Whitebox.setInternalState(listener, "hbFactory", this.hbFactory);
        Mockito.doThrow(new RuntimeException("On close")).when(this.hbFactory).close();
        Throwable caught = null;
        try {
            listener.contextDestroyed(this.contextEvent);
        } catch (RuntimeException re) {
            caught = re;
        }
        assertNotNull(caught);
        assertThat(caught.getSuppressed()[0].getMessage(), is("On close"));
    }

}