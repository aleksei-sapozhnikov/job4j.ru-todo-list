package todolist.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import todolist.model.Item;
import todolist.model.TaskBean;
import todolist.persistence.util.RollbackProxy;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ItemDatabaseStorageTest {

    private final ItemStorage storage = ItemDatabaseStorage.getInstance();
    private final SessionFactory storageFactory = Whitebox.getInternalState(this.storage, "factory");

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

    private void doIntegralTestWithRollback(Runnable operations) {
        try (var factory = RollbackProxy.create(this.storageFactory);
             var session = factory.openSession()
        ) {
            // do bad things
            Whitebox.setInternalState(this.storage, "factory", factory);
            operations.run();
            session.clear();
            // clear bad things
            Whitebox.setInternalState(this.storage, "factory", this.storageFactory);
        }
    }

    @Test
    public void whenMergeItemWithNewIdThenItemAdded() {
        this.doIntegralTestWithRollback(() -> {
            var toAdd = this.createItem("item one", 123, true);
            var added = this.storage.merge(toAdd);
            assertEquals(added.getDescription(), toAdd.getDescription());
            assertEquals(added.getCreated(), toAdd.getCreated());
            assertEquals(added.isDone(), toAdd.isDone());
            assertEquals(this.storage.getAll().size(), 1);
        });
    }

    @Test
    public void whenMergeItemWithExistingIdThenItemUpdated() {
        this.doIntegralTestWithRollback(() -> {
            var toAdd = this.createItem("item", 123, true);
            var added = this.storage.merge(toAdd);
            var toUpdate = this.createItem(added.getId(), "updated item", 567, false);
            var updated = this.storage.merge(toUpdate);
            assertEquals(updated.getId(), added.getId());
            assertEquals(updated.getDescription(), toUpdate.getDescription());
            assertEquals(updated.getCreated(), toUpdate.getCreated());
            assertEquals(updated.isDone(), toUpdate.isDone());
            assertEquals(this.storage.getAll().size(), 1);
        });
    }

    @Test
    public void whenSearchItemThenItemFound() {
        this.doIntegralTestWithRollback(() -> {
            var toAdd = this.createItem("item", 123, true);
            var added = this.storage.merge(toAdd);
            this.storage.merge(this.createItem("item2", 234, false));
            this.storage.merge(this.createItem("item3", 456, true));
            assertEquals(this.storage.getAll().size(), 3);
            //
            var search = new Item();
            search.setId(added.getId());
            var found = this.storage.get(search);
            assertEquals(found.getId(), added.getId());
            assertEquals(found.getDescription(), added.getDescription());
            assertEquals(found.getCreated(), added.getCreated());
            assertEquals(found.isDone(), added.isDone());
        });
    }

    @Test
    public void whenGetAllThenAllItemsReturned() {
        this.doIntegralTestWithRollback(() -> {
            this.storage.merge(this.createItem("item2", 234, false));
            this.storage.merge(this.createItem("item3", 456, true));
            this.storage.merge(this.createItem("item4", 987, false));
            var all = this.storage.getAll();
            var size = all.size();
            assertEquals(all.size(), size);
            //
            var description = new ArrayList<String>();
            var created = new ArrayList<Long>();
            var done = new ArrayList<Boolean>();
            all.forEach(item -> {
                description.add(item.getDescription());
                created.add(item.getCreated());
                done.add(item.isDone());
            });
            assertThat(description, org.hamcrest.Matchers.containsInAnyOrder("item2", "item3", "item4"));
            assertThat(created, org.hamcrest.Matchers.containsInAnyOrder(234L, 456L, 987L));
            assertThat(done, org.hamcrest.Matchers.containsInAnyOrder(true, false, false));
        });
    }

    @Test
    public void whenAutoClosedThenInnerSessionFactoryClosed() throws Exception {
        var mockFactory = Mockito.mock(SessionFactory.class);

        // do bad things
        var realFactory = Whitebox.getInternalState(this.storage, "factory");
        Whitebox.setInternalState(this.storage, "factory", mockFactory);

        // do checks
        this.storage.close();
        verify(mockFactory).close();

        // clear bad thing
        Whitebox.setInternalState(this.storage, "factory", realFactory);
    }

    @Test
    public void whenExceptionThenTransactionRollback() {
        var factory = Mockito.mock(SessionFactory.class);
        var session = Mockito.mock(Session.class);
        var transaction = Mockito.mock(Transaction.class);
        var searchBean = Mockito.mock(TaskBean.class);
        var resultBean = Mockito.mock(TaskBean.class);
        when(factory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(searchBean.getId()).thenReturn(5);
        when(session.get(TaskBean.class, 5)).thenReturn(resultBean);
        doThrow(new RuntimeException("We expected that!")).when(transaction).commit();

        // do bad things
        var realFactory = Whitebox.getInternalState(this.storage, "factory");
        Whitebox.setInternalState(this.storage, "factory", factory);

        // do checks
        String msg = null;
        try {
            // operation doesn't matter
            this.storage.get(searchBean);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("We expected that!", msg);
        verify(transaction).rollback();

        // clear bad thing
        Whitebox.setInternalState(this.storage, "factory", realFactory);
    }
}