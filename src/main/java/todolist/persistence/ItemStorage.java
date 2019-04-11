package todolist.persistence;

import todolist.model.TaskBean;

import java.util.List;

public interface ItemStorage {

    /**
     * Finds item by id and return Item object.
     *
     * @param task Item object with search information (id).
     * @return Item with given id from database.
     */
    TaskBean get(TaskBean task);

    /**
     * If id of given item found in database, updates item.
     * Otherwise adds given item to database with id given by database.
     *
     * @param task Item to store or to use as update.
     * @return Item object stored in database, with id given by database.
     */
    TaskBean merge(TaskBean task);

    /**
     * Returns all items stored in database.
     *
     * @return List of Item objects.
     */
    List<TaskBean> getAll();
}
