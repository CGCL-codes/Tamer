    @Override
    public void print(boolean b) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.print(b);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
