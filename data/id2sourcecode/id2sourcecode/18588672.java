    @Override
    public void println(int x) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.println(x);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
