package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import todolist.model.Item;

import java.util.List;
import java.util.function.Function;

/**
 * Database storage api using Hibernate.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ItemDbStorage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    static final Logger LOG = LogManager.getLogger(ItemDbStorage.class);

    /**
     * Hibernate session factory to store items.
     */
    private final SessionFactory factory;

    /**
     * Constructor.
     */
    public ItemDbStorage(SessionFactory factory) {
        this.factory = factory;
    }

    /**
     * Finds item by id and return Item object.
     *
     * @param item Item object with search information (id).
     * @return Item with given id from database.
     */
    public Item get(Item item) {
        return this.performTransaction(
                session -> session.get(Item.class, item.getId()));
    }

    /**
     * If id of given item found in database, updates item.
     * Otherwise adds given item to database with id given by database.
     *
     * @param item Item to store or to use as update.
     * @return Item object stored in database, with id given by database.
     */
    public Item merge(Item item) {
        return this.performTransaction(
                session -> (Item) session.merge(item));
    }

    /**
     * Returns all items stored in database.
     *
     * @return List of Item objects.
     */
    @SuppressWarnings("unchecked")
    public List<Item> getAll() {
        return this.performTransaction(
                session -> session.createQuery("from Item").list());
    }

    /**
     * Returns all items associated with given user id.
     *
     * @return List of Item objects.
     */
    @SuppressWarnings("unchecked")
    public List<Item> getForUser(long userId) {
        return this.performTransaction(
                session -> session.createQuery("from Item i where i.user.id = :userId")
                        .setParameter("userId", userId)
                        .list());
    }

    /**
     * Performs transaction: creates session, performs given operations, commits and closes.
     *
     * @param operations Function: operations to perform.
     * @param <T>        Result type.
     * @return Operation result.
     */
    private <T> T performTransaction(final Function<Session, T> operations) {
        T result;
        try (final var session = this.factory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                result = operations.apply(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
        return result;
    }

}