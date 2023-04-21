    @Override
    public void close() {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.close();
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
