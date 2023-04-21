    @Override
    public void write(byte[] buf, int off, int len) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.write(buf, off, len);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
