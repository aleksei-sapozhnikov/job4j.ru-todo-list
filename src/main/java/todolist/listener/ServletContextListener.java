package todolist.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import todolist.constants.ContextAttrs;
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
     * Hibernate factory - to close on application close.
     */
    private SessionFactory hbFactory;

    /**
     * Opens and adds resources to ServletContext on application start.
     *
     * @param sce Servlet Context Event object - to get Servlet Context from.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.hbFactory = new Configuration().configure().buildSessionFactory();
        var ctx = sce.getServletContext();
        ctx.setAttribute(ContextAttrs.ITEM_STORAGE.v(), new ItemDatabaseStorage(this.hbFactory));
    }

    /**
     * Closes resources on application start.
     *
     * @param sce Servlet Context Event object - to get Servlet Context from.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            this.hbFactory.close();
        } catch (Exception e) {
            var re = new RuntimeException("Unexpected exception on resource close");
            re.addSuppressed(e);
            throw re;
        }
    }
}
