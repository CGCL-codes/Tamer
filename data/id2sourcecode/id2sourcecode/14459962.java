    @Override
    public void write(String s, int off, int len) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.write(s, off, len);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
