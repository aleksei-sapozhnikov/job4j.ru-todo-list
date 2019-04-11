package todolist.persistence.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Interface for integral tests.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IntegralTest {
    /**
     * Rollback session factory.
     */
    SessionFactory hbFactory = new Configuration().configure().buildSessionFactory();
}
