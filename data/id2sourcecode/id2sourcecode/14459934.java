    @Override
    public void close() {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.close();
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
