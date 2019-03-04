package todolist.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOG = LogManager.getLogger(Item.class);

    private long id;
    private String description;
    private long created;
    private boolean done;

    public Item(long id, String description, long created, boolean done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    @Override
    public String toString() {
        return String.format("Item{id=%d, description='%s', created=%d, done=%s}", id, description, created, done);
    }
}
