package todolist.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Configuration;
import todolist.persistence.ItemDatabaseStorage;

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
        var factory = new Configuration().configure().buildSessionFactory();
        var storage = new ItemDatabaseStorage(
                "I know there must be only one instance created of this class in the application. I really know what I'm doing!",
                factory);
        sce.getServletContext().setAttribute("storage", storage);
    }

    /**
     * Closes resources on application start.
     *
     * @param sce Servlet Context Event object - to get Servlet Context from.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        var storage = (ItemDatabaseStorage) sce.getServletContext().getAttribute("storage");
        storage.close();
    }
}
