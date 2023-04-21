    @Override
    public PrintStream format(String format, Object... args) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.format(format, args);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
