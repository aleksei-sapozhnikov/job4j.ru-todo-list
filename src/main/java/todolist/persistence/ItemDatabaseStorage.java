package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import todolist.model.Item;

import java.util.List;

/**
 * Database storage api using Hibernate.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemDatabaseStorage implements AutoCloseable {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(ItemDatabaseStorage.class);
    /**
     * Hibernate session factory to store items.
     */
    private final SessionFactory factory;

    /**
     * Constructor with warning not to create more than one instance for storage.
     * This hopefully will prevent user from accidentally creating new storage instance.
     * Only one instance must be used for one application on one JVM.
     * <p>
     * Why not Singleton? Because of testing problems and not-so-good behaviour of PowerMock library.
     * <p>
     * Warning string to pass is:
     * 'I know there must be only one instance created of this class in the application. I really know what I'm doing!'
     *
     * @param warningAgreement Warning string parameter which makes you to agree with creation consequences.
     * @param factory          Hibernate session factory.
     * @throws RuntimeException In case of wrong warningAgreement.
     */
    public ItemDatabaseStorage(String warningAgreement, SessionFactory factory) throws RuntimeException {
        if (!("I know there must be only one instance created of this class in the application. I really know what I'm doing!").equals(warningAgreement)) {
            throw new RuntimeException("You typed agreement with warning wrong. Do you really need to create new class instance?");
        }
        LOG.info(String.format(
                "Created instance of one-instance object%s", this.getClass().getName())
        );
        this.factory = factory;
    }

    /**
     * Finds item by id and return Item object.
     *
     * @param item Item object with search information (id).
     * @return Item with given id from database.
     */
    public Item get(Item item) {
        Item result;
        try (var session = factory.openSession()) {
            session.beginTransaction();
            result = session.get(Item.class, item.getId());
            session.getTransaction().commit();
        }
        return result;
    }

    /**
     * If id of given item found in database, updates item.
     * Otherwise adds given item to database with id given by database.
     *
     * @param item Item to store or to use as update.
     * @return Item object stored in database, with id given by database.
     */
    public Item merge(Item item) {
        Item result;
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            result = (Item) session.merge(item);
            session.getTransaction().commit();
        }
        return result;
    }

    /**
     * Deletes item with given id from database.
     *
     * @param item Item to get id from.
     */
    public void delete(Item item) {
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
    }

    /**
     * Returns all items stored in database.
     *
     * @return List of Item objects.
     */
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

    /**
     * Closes resources.
     */
    @Override
    public void close() {
        this.factory.close();
    }
}
