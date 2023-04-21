    public static Logger create(Class<?> aSource, Object aRuntime) {
        return new Logger(getInstance(), getChannelFor(aSource, aRuntime), aSource, aRuntime);
    }
