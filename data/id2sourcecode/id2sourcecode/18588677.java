    @Override
    public void write(byte[] buf) throws IOException {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.write(buf);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
