    public ReadWriteConfigurationListener(final Class<?> type, final ConfigurationReaderFactory readerFactory, final ConfigurationWriterFactory writerFactory, final ExceptionListener exceptionListener) {
        this.type = type;
        this.readerFactory = readerFactory;
        this.writerFactory = writerFactory;
        this.exceptionListener = exceptionListener;
    }
