package todolist.model;

/**
 * Task bean interface.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface TaskBean {
    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    int getId();

    /**
     * Sets id value.
     *
     * @param id Value to set.
     */
    void setId(int id);

    /**
     * Returns description.
     *
     * @return Value of description field.
     */
    String getDescription();

    /**
     * Sets description value.
     *
     * @param description Value to set.
     */
    void setDescription(String description);

    /**
     * Returns created.
     *
     * @return Value of created field.
     */
    long getCreated();

    /**
     * Sets created value.
     *
     * @param created Value to set.
     */
    void setCreated(long created);

    /**
     * Returns done.
     *
     * @return Value of done field.
     */
    boolean isDone();

    /**
     * Sets done value.
     *
     * @param done Value to set.
     */
    void setDone(boolean done);
}
