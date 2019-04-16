package todolist.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Function;

/**
 * General storage class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public abstract class AbstractDbStorage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(AbstractDbStorage.class);
    /**
     * Hibernate session factory.
     */
    private final SessionFactory hbFactory;

    /**
     * Constructs new storage object.
     *
     * @param hbFactory Hibernate session factory.
     */
    protected AbstractDbStorage(SessionFactory hbFactory) {
        this.hbFactory = hbFactory;
    }

    /**
     * Performs transaction: creates session, performs given operations, commits and closes.
     *
     * @param operations Function: operations to perform.
     * @param <T>        Result type.
     * @return Operation result.
     */
    protected <T> T performTransaction(final Function<Session, T> operations) {
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
