    @Override
    public PrintWriter printf(String format, Object... args) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.printf(format, args);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
