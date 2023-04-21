    public GenericStorageException(boolean readOnly, String name, Throwable cause) {
        super("error executing " + (readOnly ? "read" : "write") + " operation \"" + name + "\"", cause);
        this.readOnly = readOnly;
        this.transactionName = name;
    }
