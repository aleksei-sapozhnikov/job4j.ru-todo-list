package todolist.persistence;

import org.junit.Test;
import todolist.model.Item;

public class ItemDatabaseStorageTest {

    @Test
    public void get() {
        var item = new Item();
        item.setId(-1);
        item.setDescription("Item one");
        item.setCreated(System.currentTimeMillis());
        item.setDone(false);
        try (var store = new ItemDatabaseStorage()) {
            var result = store.merge(item);
            System.out.println(result);
        }
    }

    @Test
    public void merge() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getAll() {
    }
}