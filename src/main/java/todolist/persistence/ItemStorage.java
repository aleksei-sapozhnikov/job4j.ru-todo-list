package todolist.persistence;

import todolist.model.Item;

import java.util.List;

public interface ItemStorage extends AutoCloseable {

    /**
     * Finds item by id and return Item object.
     *
     * @param item Item object with search information (id).
     * @return Item with given id from database.
     */
    Item get(Item item);

    /**
     * If id of given item found in database, updates item.
     * Otherwise adds given item to database with id given by database.
     *
     * @param item Item to store or to use as update.
     * @return Item object stored in database, with id given by database.
     */
    Item merge(Item item);

    /**
     * Returns all items stored in database.
     *
     * @return List of Item objects.
     */
    List<Item> getAll();
}
