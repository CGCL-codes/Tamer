    public void addAll(List<E> elements) {
        if (elements != null) {
            initFilesIfNecessary();
            int newElementCount = elements.size();
            if (newElementCount > 0) {
                RandomAccessFile randomIndexFile = null;
                RandomAccessFile randomDataFile = null;
                Lock lock = readWriteLock.writeLock();
                lock.lock();
                Throwable throwable = null;
                try {
                    randomIndexFile = new RandomAccessFile(indexFile, "rw");
                    randomDataFile = new RandomAccessFile(dataFile, "rw");
                    dataStrategy.addAll(elements, randomIndexFile, randomDataFile, codec, indexStrategy);
                    if (elementProcessors != null) {
                        for (ElementProcessor<E> current : elementProcessors) {
                            current.processElements(elements);
                        }
                    }
                } catch (Throwable e) {
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
        }
    }
