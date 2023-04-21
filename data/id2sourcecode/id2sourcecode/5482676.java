    public ReaderWriterVirtualStorage(StorageReader reader, StorageWriter writer) {
        if (null == reader) throw new NullPointerException("Reader should not be null.");
        m_listeners = new EventListenerAggregate(FileChangeListener.class);
        m_reader = reader;
        if (m_reader instanceof FileChangeListener) m_listeners.add((FileChangeListener) m_reader);
        m_writer = writer;
        if (m_writer instanceof FileChangeListener) m_listeners.add((FileChangeListener) m_writer);
    }
