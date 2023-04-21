    @Override
    public void print(String s) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.print(s);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
