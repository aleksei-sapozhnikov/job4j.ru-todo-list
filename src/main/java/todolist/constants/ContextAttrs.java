package todolist.constants;

/**
 * Parameter names for context attributes.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public enum ContextAttrs {
    BASE_DIR("baseDit"),

    LOGGED_USER("loggedUser"),

    MESSAGE("message"),

    ITEM_STORAGE("itemStorage"),
    USER_STORAGE("userStorage"),

    ADDR_LOGIN("login"),

    VIEW_LOGIN("/login.html"),

    PRM_LOGIN("login"),
    PRM_PASSWORD("password"),

    JSON_PARSER("jsonParser"),
    MAPPER("mapper");

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
