package todolist;

public enum Constants {
    CONTEXT_ATTR_STORAGE("storage");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
