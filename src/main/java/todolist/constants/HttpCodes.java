package todolist.constants;

/**
 * HTTP response codes constants.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public enum HttpCodes {
    /**
     * HTTP status codes.
     */
    CREATED(201);

    /**
     * Value holder.
     */
    private final int value;

    /**
     * Constructor.
     *
     * @param value Value to save.
     */
    HttpCodes(int value) {
        this.value = value;
    }

    /**
     * Returns value.
     *
     * @return Value of value field.
     */
    public int v() {
        return this.value;
    }
}
