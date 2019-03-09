package todolist.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import todolist.constants.ContextAttrs;
import todolist.persistence.ItemDatabaseStorage;
import todolist.persistence.ItemStorage;

import javax.servlet.ServletContextEvent;

/**
 * Application launch/destroy listener to open/close database connection.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ServletContextListener implements javax.servlet.ServletContextListener {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(ServletContextListener.class);

    /**
     * Opens and adds resources to ServletContext on application start.
     *
     * @param sce Servlet Context Event object - to get Servlet Context from.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(
                ContextAttrs.STORAGE.v(),
                ItemDatabaseStorage.INSTANCE);
    }

    /**
     * Closes resources on application start.
     *
     * @param sce Servlet Context Event object - to get Servlet Context from.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        var storage = (ItemStorage) sce.getServletContext()
                .getAttribute(ContextAttrs.STORAGE.v());
        try {
            storage.close();
        } catch (Exception e) {
            var re = new RuntimeException("Unexpected exception on resource close");
            re.addSuppressed(e);
            throw re;
        }
    }
}
