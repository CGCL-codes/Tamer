    @Override
    public void print(int i) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.print(i);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
