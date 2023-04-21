    public PropertyAccessException(String property, boolean readOrWrite, Exception cause) {
        super("Cannot " + (readOrWrite ? "read" : "write") + " " + property + " " + cause);
    }
