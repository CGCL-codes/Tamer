    @Override
    public PrintWriter format(String format, Object... args) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.format(format, args);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
