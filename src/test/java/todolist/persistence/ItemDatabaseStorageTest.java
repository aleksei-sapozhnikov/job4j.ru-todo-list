package todolist.persistence;

import org.junit.After;
import org.junit.Test;
import todolist.model.Item;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ItemDatabaseStorageTest {

    private final ItemDatabaseStorage storage = ItemDatabaseStorage.INSTANCE;

    private Item createItem(int id, String description, long created, boolean done) {
        var item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setCreated(created);
        item.setDone(done);
        return item;
    }

    private Item createItem(String description, long created, boolean done) {
        return this.createItem(0, description, created, done);
    }

    @After
    public void clearTable() {
        this.storage.clear();
    }

    @Test
    public void whenMergeItemWithNewIdThenItemAdded() {
        var added = this.createItem("item one", 123, true);
        this.storage.merge(added);
        var all = this.storage.getAll();
        assertEquals(all.size(), 1);
        //
        var found = all.get(0);
        assertEquals(found.getDescription(), "item one");
        assertEquals(found.getCreated(), 123);
        assertTrue(found.isDone());
    }

    @Test
    public void whenMergeItemWithExistingIdThenItemUpdated() {
        var added = this.createItem("item", 123, true);
        this.storage.merge(added);
        var addedId = this.storage.getAll().get(0).getId();
        var update = this.createItem(addedId, "updated item", 567, false);
        this.storage.merge(update);
        var all = this.storage.getAll();
        assertEquals(all.size(), 1);
        //
        var found = all.get(0);
        assertEquals(found.getDescription(), "updated item");
        assertEquals(found.getCreated(), 567);
        assertFalse(found.isDone());
    }

    @Test
    public void whenSearchItemThenItemFound() {
        var needed = this.createItem("item", 123, true);
        this.storage.merge(needed);
        var neededId = this.storage.getAll().get(0).getId();
        this.storage.merge(this.createItem("item2", 234, false));
        this.storage.merge(this.createItem("item3", 456, true));
        assertEquals(this.storage.getAll().size(), 3);
        //
        var search = new Item();
        search.setId(neededId);
        var found = this.storage.get(search);
        assertEquals(found.getId(), neededId);
        assertEquals(found.getDescription(), needed.getDescription());
        assertEquals(found.getCreated(), needed.getCreated());
        assertEquals(found.isDone(), needed.isDone());
    }

    @Test
    public void whenGetAllThenAllItemsReturned() {
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
    }
}