    @Override
    public void print(char c) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.print(c);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
