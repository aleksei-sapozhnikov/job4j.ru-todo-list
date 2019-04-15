package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import todolist.model.User;

/**
 * User database storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class UserDbStorage extends AbstractDbStorage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(UserDbStorage.class);

    /**
     * Constructs user storage object.
     *
     * @param hbFactory Hibernate session factory.
     */
    public UserDbStorage(SessionFactory hbFactory) {
        super(hbFactory);
    }

    public User getOrAdd(User user) {
        return this.performTransaction(session -> {
            User result = session.get(User.class, user.getLogin());
            if (result == null) {
                result = user;
                session.persist(result);
            }
            return result;
        });
    }
}
