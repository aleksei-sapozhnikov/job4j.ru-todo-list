package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import todolist.model.Item;

/**
 * Database storage api.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DbStorage {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DbStorage.class);

    public static void main(String[] args) throws Exception {
        try (var factory = new Configuration().configure().buildSessionFactory()) {
            // addItem(factory, id, description, created, done);
            // Item found = getItem(factory, id);
            // deleteItem(factory, id);/

            var item = new Item();
            item.setId(-1);
            item.setDescription("My first item");
            item.setCreated(System.currentTimeMillis());
            item.setDone(false);

            System.out.printf("Adding: %s%n", item);
            addItem(factory, item);
            System.out.printf("Stored: %s%n", item);
            System.out.printf("Found: %s%n", getItem(factory, item.getId()));

            item.setDone(true);
            System.out.printf("Updating: %s%n", item);
            update(factory, item);
            System.out.printf("Found: %s%n", getItem(factory, item.getId()));
        }
    }

    private static void addItem(SessionFactory factory, Item item) {
        try (var session = factory.openSession()) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        }
    }

    private static void update(SessionFactory factory, Item item) {
        try (var session = factory.openSession()) {
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        }
    }

    private static Item getItem(SessionFactory factory, int id) {
        Item found;
        try (var session = factory.openSession()) {
            session.beginTransaction();
            found = session.get(Item.class, id);
            session.getTransaction().commit();
        }
        return found;
    }

    private static void deleteItem(SessionFactory factory, Item item) {
        try (var session = factory.openSession()) {
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
    }
}
