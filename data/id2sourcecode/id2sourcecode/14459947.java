    @Override
    public PrintWriter printf(Locale l, String format, Object... args) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.printf(l, format, args);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
