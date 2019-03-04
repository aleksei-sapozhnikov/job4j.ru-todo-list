package todolist.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Item model.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Item implements Comparable<Item> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Item.class);

    private int id;
    private String description;
    private long created;
    private boolean done;

    public Item(int id, String description, long created, boolean done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    @Override
    public String toString() {
        return String.format("Item{id=%d, description='%s', created=%d, done=%s}", id, description, created, done);
    }

    @Override
    public int compareTo(Item o) {
        var result = this.id - o.id;
        return result == 0 ? result : (int) (this.created - o.created);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return id == item.id;
    }

    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
