    @Override
    public void print(Object obj) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.print(obj);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
