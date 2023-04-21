    private boolean initFilesIfNecessary() {
        if (!dataFile.exists() || dataFile.length() < fileHeaderStrategy.getMinimalSize()) {
            Lock lock = readWriteLock.writeLock();
            lock.lock();
            try {
                dataFile.delete();
                setFileHeader(fileHeaderStrategy.writeFileHeader(dataFile, magicValue, preferredMetaData, preferredSparse));
                indexFile.delete();
                return true;
            } catch (IOException e) {
                if (logger.isWarnEnabled()) logger.warn("Exception while initializing file!", e);
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
