    @Override
    public void print(long l) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.print(l);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
