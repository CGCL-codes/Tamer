    @Override
    public void print(double d) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.print(d);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
