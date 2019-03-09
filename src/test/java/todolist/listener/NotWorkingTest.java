package todolist.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import todolist.constants.ContextAttrs;
import todolist.persistence.ItemDatabaseStorage;
import todolist.persistence.ItemStorage;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*", "javax.management.*"})
@PrepareForTest(ItemDatabaseStorage.class)
public class NotWorkingTest {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(NotWorkingTest.class);

    private final ItemStorage storage = mock(ItemStorage.class);

    private final ServletContextEvent contextEvent = mock(ServletContextEvent.class);
    private final ServletContext context = mock(ServletContext.class);

    @Test
    @Ignore
    public void whenContextInitializedThenStorageSetAsParameter() {
        Whitebox.setInternalState(ItemDatabaseStorage.class, "INSTANCE", this.storage);
        when(this.contextEvent.getServletContext()).thenReturn(this.context);
        new ServletContextListener().contextInitialized(contextEvent);
        verify(this.context).setAttribute(
                eq(ContextAttrs.STORAGE.v()),
                any(ItemStorage.class));
    }
}
