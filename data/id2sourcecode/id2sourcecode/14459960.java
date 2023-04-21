    @Override
    public void write(char[] buf) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.write(buf);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
