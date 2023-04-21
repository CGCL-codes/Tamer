    @Override
    public void println(String x) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.println(x);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
