package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import todolist.model.Item;

import java.util.List;

/**
 * Database storage api.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemDatabaseStorage implements AutoCloseable {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ItemDatabaseStorage.class);

    private final SessionFactory factory = new Configuration().configure().buildSessionFactory();

    public Item get(Item item) {
        Item result;
        try (var session = factory.openSession()) {
            session.beginTransaction();
            result = session.get(Item.class, item.getId());
            session.getTransaction().commit();
        }
        return result;
    }

    public Item merge(Item item) {
        Item result;
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            result = (Item) session.merge(item);
            session.getTransaction().commit();
        }
        return result;
    }

    public void delete(Item item) {
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Item> getAll() {
        List<Item> result;
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            var query = session.createQuery("from Item");
            result = query.list();
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public void close() {
        this.factory.close();
    }
}
