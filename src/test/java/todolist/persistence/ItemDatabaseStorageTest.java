package todolist.persistence;

import org.junit.After;
import org.junit.Test;
import todolist.model.Item;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        var toAdd = this.createItem("item one", 123, true);
        var added = this.storage.merge(toAdd);
        assertEquals(added.getDescription(), toAdd.getDescription());
        assertEquals(added.getCreated(), toAdd.getCreated());
        assertEquals(added.isDone(), toAdd.isDone());
        assertEquals(this.storage.getAll().size(), 1);
    }

    @Test
    public void whenMergeItemWithExistingIdThenItemUpdated() {
        var toAdd = this.createItem("item", 123, true);
        var added = this.storage.merge(toAdd);
        var toUpdate = this.createItem(added.getId(), "updated item", 567, false);
        var updated = this.storage.merge(toUpdate);
        assertEquals(updated.getId(), added.getId());
        assertEquals(updated.getDescription(), toUpdate.getDescription());
        assertEquals(updated.getCreated(), toUpdate.getCreated());
        assertEquals(updated.isDone(), toUpdate.isDone());
        assertEquals(this.storage.getAll().size(), 1);
    }

    @Test
    public void whenSearchItemThenItemFound() {
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