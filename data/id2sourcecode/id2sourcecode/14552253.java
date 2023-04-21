    public static Logger create(Class<?> aSource) {
        return new Logger(getInstance(), getChannelFor(aSource, null), aSource);
    }
