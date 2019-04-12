package todolist.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.mockito.Mockito;
import todolist.model.Item;
import todolist.model.User;
import todolist.persistence.util.RollbackProxy;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class IntegralStorageTest {

    /**
     * Rollback session factory.
     */
    private final SessionFactory hbFactory = new Configuration().configure().buildSessionFactory();

    private Item createItem(int id, String description, long created, boolean done, User user) {
        var item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setCreated(created);
        item.setDone(done);
        item.setUser(user);
        return item;
    }

    private Item createItem(String description, long created, boolean done, User user) {
        return this.createItem(0, description, created, done, user);
    }

    /**
     * Do integral test with rollback.
     *
     * @param operations Test operations.
     */
    private void doIntegralTestWithRollback(BiConsumer<ItemDbStorage, UserDbStorage> operations) {
        try (var factory = RollbackProxy.create(hbFactory)) {
            var itemStorage = new ItemDbStorage(factory);
            var userStorage = new UserDbStorage(factory);
            operations.accept(itemStorage, userStorage);
        }
    }

    @Test
    public void whenMergeItemWithNewIdThenItemAdded() {
        this.doIntegralTestWithRollback((itemStorage, userStorage) -> {
            var user = userStorage.merge(new User().setLogin("default").setPassword("default"));
            var toAdd = this.createItem("item one", 123, true, user);
            var added = itemStorage.merge(toAdd);
            assertEquals(added.getDescription(), toAdd.getDescription());
            assertEquals(added.getCreated(), toAdd.getCreated());
            assertEquals(added.isDone(), toAdd.isDone());
            assertEquals(itemStorage.getAll().size(), 1);
        });
    }

    @Test
    public void whenMergeItemWithExistingIdThenItemUpdated() {
        this.doIntegralTestWithRollback((itemStorage, userStorage) -> {
            var user = userStorage.merge(new User().setLogin("default").setPassword("default"));
            var toAdd = this.createItem("item", 123, true, user);
            var added = itemStorage.merge(toAdd);
            var toUpdate = this.createItem(added.getId(), "updated item", 567, false, user);
            var updated = itemStorage.merge(toUpdate);
            assertEquals(updated.getId(), added.getId());
            assertEquals(updated.getDescription(), toUpdate.getDescription());
            assertEquals(updated.getCreated(), toUpdate.getCreated());
            assertEquals(updated.isDone(), toUpdate.isDone());
            assertEquals(itemStorage.getAll().size(), 1);
        });
    }

    @Test
    public void whenSearchItemThenItemFound() {
        this.doIntegralTestWithRollback((itemStorage, userStorage) -> {
            var user = userStorage.merge(new User().setLogin("default").setPassword("default"));
            var toAdd = this.createItem("item", 123, true, user);
            var added = itemStorage.merge(toAdd);
            itemStorage.merge(this.createItem("item2", 234, false, user));
            itemStorage.merge(this.createItem("item3", 456, true, user));
            assertEquals(itemStorage.getAll().size(), 3);
            //
            var search = new Item();
            search.setId(added.getId());
            var found = itemStorage.get(search);
            assertEquals(found.getId(), added.getId());
            assertEquals(found.getDescription(), added.getDescription());
            assertEquals(found.getCreated(), added.getCreated());
            assertEquals(found.isDone(), added.isDone());
        });
    }

    @Test
    public void whenGetAllThenAllItemsReturned() {
        this.doIntegralTestWithRollback((itemStorage, userStorage) -> {
            var user = userStorage.merge(new User().setLogin("default").setPassword("default"));
            itemStorage.merge(this.createItem("item2", 234, false, user));
            itemStorage.merge(this.createItem("item3", 456, true, user));
            itemStorage.merge(this.createItem("item4", 987, false, user));
            var all = itemStorage.getAll();
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
    public void whenGetForUserThenItemsForUserReturned() {
        try (var factory = RollbackProxy.create(hbFactory)) {
            var itemStorage = new ItemDbStorage(factory);
            var userStorage = new UserDbStorage(factory);
            // add users
            var userOne = new User().setId(1).setLogin("login one").setPassword("password one");
            var userTwo = new User().setId(2).setLogin("login two").setPassword("password two");
            userOne = userStorage.merge(userOne);
            userTwo = userStorage.merge(userTwo);
            // add items
            itemStorage.merge(this.createItem("item2 user one", 234, false, userOne));
            itemStorage.merge(this.createItem("item3 user two", 456, true, userTwo));
            itemStorage.merge(this.createItem("item4 user two", 474, false, userTwo));
            itemStorage.merge(this.createItem("item5 user one", 247, false, userOne));
            itemStorage.merge(this.createItem("item6 user two", 754, false, userTwo));
            // get for users
            var forUserOne = itemStorage.getForUser(userOne.getId());
            var forUserTwo = itemStorage.getForUser(userTwo.getId());
            assertEquals(2, forUserOne.size());
            assertEquals(3, forUserTwo.size());
            // check values for userOne
            var descriptionOne = new ArrayList<String>();
            forUserOne.forEach(item -> descriptionOne.add(item.getDescription()));
            assertThat(descriptionOne,
                    org.hamcrest.Matchers.containsInAnyOrder("item2 user one", "item5 user one"));
            // check values for userTwo
            var descriptionTwo = new ArrayList<String>();
            forUserTwo.forEach(item -> descriptionTwo.add(item.getDescription()));
            assertThat(descriptionTwo,
                    org.hamcrest.Matchers.containsInAnyOrder("item3 user two", "item4 user two", "item6 user two"));
        }
    }

    @Test
    public void whenExceptionThenTransactionRollback() {
        // mocks
        var factory = Mockito.mock(SessionFactory.class);
        var session = Mockito.mock(Session.class);
        var transaction = Mockito.mock(Transaction.class);
        when(factory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        doThrow(new RuntimeException("We expected that!")).when(transaction).commit();
        // actions
        var storage = new AbstractDbStorage(factory) {
        };
        String msg = null;
        try {
            // operation doesn't matter
            storage.performTransaction(ses -> "some result");
        } catch (Exception e) {
            msg = e.getMessage();
        }
        // verify
        assertEquals("We expected that!", msg);
        verify(transaction).rollback();
    }
}