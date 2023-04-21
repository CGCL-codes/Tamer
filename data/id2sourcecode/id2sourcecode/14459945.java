    @Override
    public void print(Object obj) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.print(obj);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
