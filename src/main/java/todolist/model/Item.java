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
public class Item {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Item.class);

    /**
     * Item id.
     */
    private int id;
    /**
     * Item description.
     */
    private String description;
    /**
     * Time of creation (milliseconds).
     */
    private long created;
    /**
     * Flag 'done'/'not done'.
     */
    private boolean done;

    /* * * * * * * * * * * * * *
     * OBJECT UTILITY METHODS  *
     * * * * * * * * * * * * * */

    /**
     * Override Object.equals().
     *
     * @param o Other object.
     * @return <tt>true</tt> if equal, <tt>false if not</tt>
     */
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
     * Override Object.hashcode().
     *
     * @return Object hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Override Object.toString().
     *
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        return String.format("Item{id=%d, description='%s', created=%d, done=%s}", id, description, created, done);
    }

    /* * * * * * * * * * * *
     * SETTERS AND GETTERS *
     * * * * * * * * * * * */

    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets id value.
     *
     * @param id Value to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns description.
     *
     * @return Value of description field.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets description value.
     *
     * @param description Value to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns created.
     *
     * @return Value of created field.
     */
    public long getCreated() {
        return this.created;
    }

    /**
     * Sets created value.
     *
     * @param created Value to set.
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Returns done.
     *
     * @return Value of done field.
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Sets done value.
     *
     * @param done Value to set.
     */
    public void setDone(boolean done) {
        this.done = done;
    }
}
