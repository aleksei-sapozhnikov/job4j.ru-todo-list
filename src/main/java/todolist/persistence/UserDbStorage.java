package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import todolist.model.User;

import java.util.function.Function;

/**
 * User database storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class UserDbStorage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(UserDbStorage.class);
    /**
     * Hibernate session factory.
     */
    private final SessionFactory hbFactory;

    /**
     * Constructs user storage object.
     *
     * @param hbFactory Hibernate session factory.
     */
    public UserDbStorage(SessionFactory hbFactory) {
        this.hbFactory = hbFactory;
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

    /**
     * Performs transaction: creates session, performs given operations, commits and closes.
     *
     * @param operations Function: operations to perform.
     * @param <T>        Result type.
     * @return Operation result.
     */
    private <T> T performTransaction(final Function<Session, T> operations) {
        T result;
        try (final var session = this.hbFactory.openSession()) {
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
