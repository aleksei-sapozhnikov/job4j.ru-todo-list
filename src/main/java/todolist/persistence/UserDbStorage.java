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

    /**
     * If id of given user found in database, updates user.
     * Otherwise adds given user to database with id given by database.
     *
     * @param user User to store or to use as update.
     * @return User object stored in database, with id given by database.
     */
    public User merge(User user) {
        return this.performTransaction(
                session -> (User) session.merge(user));
    }


}
