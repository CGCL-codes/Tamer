    public ManualMappingDefinition(final Class baseClass, final String readHandler, final String writeHandler) {
        if (baseClass == null) {
            throw new NullPointerException("BaseClass must not be null");
        }
        if (readHandler == null && writeHandler == null) {
            throw new NullPointerException("At least one of readHandler or writeHandler must be defined.");
        }
        this.baseClass = baseClass;
        this.readHandler = readHandler;
        this.writeHandler = writeHandler;
    }
