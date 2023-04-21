    @Override
    public void println(float x) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.println(x);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
