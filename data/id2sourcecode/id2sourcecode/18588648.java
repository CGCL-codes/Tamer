    @Override
    public PrintStream append(char c) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.append(c);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
