package todolist.persistence.study;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

import java.lang.reflect.Proxy;

/**
 * Class for study proxy work. Not used in application parts.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class RollbackProxy {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(RollbackProxy.class);

    /**
     * Returns proxy which calls 'rollback' instead of 'commit'.
     *
     * @param tx Transaction to wrap.
     * @return Proxy object.
     */
    public static Transaction rollbackTransaction(org.hibernate.Transaction tx) {
        return (Transaction) Proxy.newProxyInstance(
                RollbackProxy.class.getClassLoader(),
                new Class[]{
                        Transaction.class
                },
                (proxy, method, args) -> {
                    Object result = null;
                    if ("commit".equals(method.getName())) {
                        tx.rollback();
                    } else {
                        result = method.invoke(tx, args);
                    }
                    return result;
                }
        );
    }
}
