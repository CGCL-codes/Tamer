    @Override
    public void println(double x) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.println(x);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
