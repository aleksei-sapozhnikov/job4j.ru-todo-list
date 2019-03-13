package todolist.listener;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import todolist.constants.ContextAttrs;
import todolist.persistence.ItemDatabaseStorage;
import todolist.persistence.ItemStorage;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ServletContextListenerTest {

    private final ItemStorage storage = mock(ItemStorage.class);

    private final ServletContextEvent contextEvent = mock(ServletContextEvent.class);
    private final ServletContext context = mock(ServletContext.class);

    @Test
    public void whenContextInitializedThenStorageSetAsParameter() {
        // do bad thing
        Whitebox.setInternalState(ItemDatabaseStorage.class, "instance", this.storage);

        when(this.contextEvent.getServletContext()).thenReturn(this.context);
        new ServletContextListener().contextInitialized(contextEvent);
        verify(this.context).setAttribute(
                eq(ContextAttrs.STORAGE.v()),
                any(ItemStorage.class));

        // clear bad thing
        Whitebox.setInternalState(ItemDatabaseStorage.class, "instance", (ItemStorage) null);
    }

    @Test
    public void whenContextDestroyedThenStorageClosed() throws Exception {
        when(this.contextEvent.getServletContext()).thenReturn(this.context);
        when(this.context.getAttribute(ContextAttrs.STORAGE.v())).thenReturn(this.storage);
        new ServletContextListener().contextDestroyed(contextEvent);
        verify(this.storage).close();
    }

    @Test
    public void whenStorageThrowsExceptionOnCloseThenRuntimeExceptionThrownWithSuppressed() throws Exception {
        when(this.contextEvent.getServletContext()).thenReturn(this.context);
        when(this.context.getAttribute(ContextAttrs.STORAGE.v())).thenReturn(this.storage);
        Mockito.doThrow(new Exception("On close")).when(this.storage).close();
        Throwable caught = null;
        try {
            new ServletContextListener().contextDestroyed(contextEvent);
        } catch (RuntimeException re) {
            caught = re;
        }
        assertNotNull(caught);
        assertThat(caught.getSuppressed()[0].getMessage(), is("On close"));
    }

}