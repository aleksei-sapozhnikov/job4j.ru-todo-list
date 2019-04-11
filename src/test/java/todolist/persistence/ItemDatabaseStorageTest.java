package todolist.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import todolist.model.Item;
import todolist.model.TaskBean;
import todolist.persistence.util.IntegralTest;
import todolist.persistence.util.RollbackProxy;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ItemDatabaseStorageTest implements IntegralTest {

    private final ItemStorage storage = new ItemDatabaseStorage(IntegralTest.hbFactory);

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

    /**
     * Do integral test with rollback.
     *
     * @param operations Test operations.
     */
    private void doIntegralTestWithRollback(Consumer<ItemDatabaseStorage> operations) {
        try (var factory = RollbackProxy.create(hbFactory)) {
            var storage = new ItemDatabaseStorage(factory);
            operations.accept(storage);
        }
    }

    @Test
    public void whenMergeItemWithNewIdThenItemAdded() {
        this.doIntegralTestWithRollback((storage) -> {
            var toAdd = this.createItem("item one", 123, true);
            var added = storage.merge(toAdd);
            assertEquals(added.getDescription(), toAdd.getDescription());
            assertEquals(added.getCreated(), toAdd.getCreated());
            assertEquals(added.isDone(), toAdd.isDone());
            assertEquals(storage.getAll().size(), 1);
        });
    }

    @Test
    public void whenMergeItemWithExistingIdThenItemUpdated() {
        this.doIntegralTestWithRollback((storage) -> {
            var toAdd = this.createItem("item", 123, true);
            var added = storage.merge(toAdd);
            var toUpdate = this.createItem(added.getId(), "updated item", 567, false);
            var updated = storage.merge(toUpdate);
            assertEquals(updated.getId(), added.getId());
            assertEquals(updated.getDescription(), toUpdate.getDescription());
            assertEquals(updated.getCreated(), toUpdate.getCreated());
            assertEquals(updated.isDone(), toUpdate.isDone());
            assertEquals(storage.getAll().size(), 1);
        });
    }

    @Test
    public void whenSearchItemThenItemFound() {
        this.doIntegralTestWithRollback((storage) -> {
            var toAdd = this.createItem("item", 123, true);
            var added = storage.merge(toAdd);
            storage.merge(this.createItem("item2", 234, false));
            storage.merge(this.createItem("item3", 456, true));
            assertEquals(storage.getAll().size(), 3);
            //
            var search = new Item();
            search.setId(added.getId());
            var found = storage.get(search);
            assertEquals(found.getId(), added.getId());
            assertEquals(found.getDescription(), added.getDescription());
            assertEquals(found.getCreated(), added.getCreated());
            assertEquals(found.isDone(), added.isDone());
        });
    }

    @Test
    public void whenGetAllThenAllItemsReturned() {
        this.doIntegralTestWithRollback((storage) -> {
            storage.merge(this.createItem("item2", 234, false));
            storage.merge(this.createItem("item3", 456, true));
            storage.merge(this.createItem("item4", 987, false));
            var all = storage.getAll();
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