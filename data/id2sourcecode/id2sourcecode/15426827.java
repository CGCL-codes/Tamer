    public void reset() {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            indexFile.delete();
            dataFile.delete();
            fileHeaderStrategy.writeFileHeader(dataFile, magicValue, preferredMetaData, preferredSparse);
            if (elementProcessors != null) {
                for (ElementProcessor<E> current : elementProcessors) {
                    Reset.reset(current);
                }
            }
        } catch (IOException e) {
            if (logger.isWarnEnabled()) logger.warn("Exception while resetting file!", e);
        } finally {
            lock.unlock();
        }
    }
