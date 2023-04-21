    @Override
    public void print(boolean b) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.print(b);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
