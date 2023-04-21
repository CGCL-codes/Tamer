    @Override
    public PrintStream printf(String format, Object... args) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.printf(format, args);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
