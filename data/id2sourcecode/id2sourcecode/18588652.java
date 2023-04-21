    @Override
    public void flush() {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.flush();
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
