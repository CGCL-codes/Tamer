    @Override
    public PrintWriter format(Locale l, String format, Object... args) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.format(l, format, args);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
