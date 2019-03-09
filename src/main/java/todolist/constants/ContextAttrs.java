package todolist.constants;

/**
 * Parameter names for context attributes.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public enum ContextAttrs {
    STORAGE("storage");

    /**
     * Value holder.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param value Value to save.
     */
    ContextAttrs(String value) {
        this.value = value;
    }

    /**
     * Returns value.
     *
     * @return Value of value field.
     */
    public String v() {
        return this.value;
    }
}
