package todolist.persistence.study;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import todolist.model.Item;
import todolist.model.TaskBean;
import todolist.persistence.ItemDatabaseStorage;

import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;


public class RollbackProxyTest {

    private final SessionFactory factory = Whitebox.getInternalState(ItemDatabaseStorage.getInstance(), "factory");

    private TaskBean createItem(int id, String description, long created, boolean done) {
        var item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setCreated(created);
        item.setDone(done);
        return item;
    }

    private TaskBean createItem(String description, long created, boolean done) {
        return this.createItem(0, description, created, done);
    }

    private void doChecksAndRollbackByProxy(Consumer<Session> operation) {
        try (var session = this.factory.openSession()) {
            var tx = RollbackProxy.rollbackTransaction(session.beginTransaction());
            try {
                operation.accept(session);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    private void doChecksAndRollbackWithoutProxy(Consumer<Session> operation) {
        try (var session = this.factory.openSession()) {
            var tx = RollbackProxy.rollbackTransaction(session.beginTransaction());
            try {
                operation.accept(session);
                tx.rollback();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Test
    public void integralTestMergeWithProxy() {
        this.doChecksAndRollbackByProxy(session -> {
            var toAdd = this.createItem("item one", 123, true);
            var added = (TaskBean) session.merge(toAdd);
            assertEquals(added.getDescription(), toAdd.getDescription());
            assertEquals(added.getCreated(), toAdd.getCreated());
            assertEquals(added.isDone(), toAdd.isDone());
            assertEquals(1, session.createQuery("from Item").list().size());
        });
    }

    @Test
    public void integralTestMergeWithoutProxy() {
        this.doChecksAndRollbackWithoutProxy(session -> {
            var toAdd = this.createItem("item one", 123, true);
            var added = (TaskBean) session.merge(toAdd);
            assertEquals(added.getDescription(), toAdd.getDescription());
            assertEquals(added.getCreated(), toAdd.getCreated());
            assertEquals(added.isDone(), toAdd.isDone());
            assertEquals(1, session.createQuery("from Item").list().size());
        });
    }

}