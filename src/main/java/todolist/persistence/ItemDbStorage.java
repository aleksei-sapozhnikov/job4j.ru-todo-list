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
public class ItemDbStorage extends AbstractDbStorage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    static final Logger LOG = LogManager.getLogger(ItemDbStorage.class);

    /**
     * Constructor.
     */
    public ItemDbStorage(SessionFactory factory) {
        super(factory);
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
    public List<Item> getForUser(String login) {
        return this.performTransaction(
                session -> session.createQuery("from Item i where i.user.login = :login")
                        .setParameter("login", login)
                        .list());
    }

}