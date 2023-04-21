    public void add(E element) {
        initFilesIfNecessary();
        RandomAccessFile randomIndexFile = null;
        RandomAccessFile randomDataFile = null;
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        Throwable throwable = null;
        try {
            randomIndexFile = new RandomAccessFile(indexFile, "rw");
            randomDataFile = new RandomAccessFile(dataFile, "rw");
            dataStrategy.add(element, randomIndexFile, randomDataFile, codec, indexStrategy);
            List<ElementProcessor<E>> localProcessors = elementProcessors;
            if (localProcessors != null) {
                for (ElementProcessor<E> current : elementProcessors) {
                    current.processElement(element);
                }
            }
        } catch (IOException e) {
            throwable = e;
        } finally {
            closeQuietly(randomDataFile);
            closeQuietly(randomIndexFile);
            lock.unlock();
        }
        if (throwable != null) {
            if (logger.isWarnEnabled()) logger.warn("Couldn't write element!", throwable);
        }
    }
