    @Override
    public void write(char[] buf, int off, int len) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.write(buf, off, len);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
