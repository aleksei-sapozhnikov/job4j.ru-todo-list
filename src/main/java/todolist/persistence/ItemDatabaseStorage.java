package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import todolist.model.Item;
import todolist.model.TaskBean;

import java.util.List;

/**
 * Database storage api using Hibernate.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public enum ItemDatabaseStorage implements ItemStorage {
    INSTANCE;

    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    static final Logger LOG = LogManager.getLogger(ItemDatabaseStorage.class);

    /**
     * Hibernate session factory to store items.
     */
    private final SessionFactory factory;

    /**
     * Constructor.
     */
    ItemDatabaseStorage() {
        this.factory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * Finds item by id and return Item object.
     *
     * @param task Item object with search information (id).
     * @return Item with given id from database.
     */
    @Override
    public TaskBean get(TaskBean task) {
        TaskBean result;
        try (var session = factory.openSession()) {
            session.beginTransaction();
            result = session.get(Item.class, task.getId());
            session.getTransaction().commit();
        }
        return result;
    }

    /**
     * If id of given item found in database, updates item.
     * Otherwise adds given item to database with id given by database.
     *
     * @param task Item to store or to use as update.
     * @return Item object stored in database, with id given by database.
     */
    @Override
    public TaskBean merge(TaskBean task) {
        TaskBean result;
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            result = (TaskBean) session.merge(task);
            session.getTransaction().commit();
        }
        return result;
    }

    /**
     * Returns all items stored in database.
     *
     * @return List of Item objects.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<TaskBean> getAll() {
        List<TaskBean> result;
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            var query = session.createQuery("from Item");
            result = query.list();
            session.getTransaction().commit();
        }
        return result;
    }

    /**
     * Clears table with entities.
     * Not part of interface.
     * For test purposes only.
     */
    void clear() {
        try (var session = this.factory.openSession()) {
            session.beginTransaction();
            var query = session.createQuery("delete from Item");
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    /**
     * Closes resources.
     */
    @Override
    public void close() {
        this.factory.close();
    }
}
