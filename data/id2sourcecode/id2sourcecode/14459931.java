    @Override
    public PrintWriter append(char c) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.append(c);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
