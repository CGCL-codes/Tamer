    @Override
    public void write(int c) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.write(c);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
