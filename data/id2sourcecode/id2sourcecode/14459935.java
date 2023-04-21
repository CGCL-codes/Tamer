    @Override
    public void flush() {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.flush();
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
