    @Override
    public void println(String x) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.println(x);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
