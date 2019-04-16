package todolist.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Frontend item object.
 * To send as JSON.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FrontItem {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(FrontItem.class);

    /**
     * Item id.
     */
    private final long id;
    /**
     * Item description.
     */
    private final String description;
    /**
     * Time of creation (milliseconds).
     */
    private final long created;
    /**
     * Flag 'done'/'not done'.
     */
    private final boolean done;

    /**
     * Constructor.
     *
     * @param id          Item id.
     * @param description Item description.
     * @param created     Time of creation (milliseconds).
     * @param done        Flag 'done'/'not done'.
     */
    public FrontItem(long id, String description, long created, boolean done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    /////////////////////
    // equals, hashcode
    /////////////////////

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
        FrontItem frontItem = (FrontItem) o;
        return id == frontItem.id;
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


    ////////////////////////
    // getters and setters
    ////////////////////////

    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    public long getId() {
        return this.id;
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
     * Returns created.
     *
     * @return Value of created field.
     */
    public long getCreated() {
        return this.created;
    }

    /**
     * Returns done.
     *
     * @return Value of done field.
     */
    public boolean isDone() {
        return this.done;
    }
}
