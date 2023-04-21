    public SoftValueMap(int initialCapacity, final float loadFactor, final int readClearCheckFrequency, final int writeClearCheckFrequency) {
        if (initialCapacity < 0) throw new IllegalArgumentException("negative input: initialCapacity [" + initialCapacity + "]");
        if ((loadFactor <= 0.0) || (loadFactor >= 1.0 + 1.0E-6)) throw new IllegalArgumentException("loadFactor not in (0.0, 1.0] range: " + loadFactor);
        if (readClearCheckFrequency < 1) throw new IllegalArgumentException("readClearCheckFrequency not in [1, +inf) range: " + readClearCheckFrequency);
        if (writeClearCheckFrequency < 1) throw new IllegalArgumentException("writeClearCheckFrequency not in [1, +inf) range: " + writeClearCheckFrequency);
        if (initialCapacity == 0) initialCapacity = 1;
        m_valueReferenceQueue = new ReferenceQueue();
        m_loadFactor = loadFactor;
        m_sizeThreshold = (int) (initialCapacity * loadFactor);
        m_readClearCheckFrequency = readClearCheckFrequency;
        m_writeClearCheckFrequency = writeClearCheckFrequency;
        m_buckets = new SoftEntry[initialCapacity];
    }
