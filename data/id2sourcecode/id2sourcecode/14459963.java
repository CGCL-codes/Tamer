    @Override
    public void write(String s) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.write(s);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
