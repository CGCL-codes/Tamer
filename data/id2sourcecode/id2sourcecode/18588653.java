    @Override
    public PrintStream format(Locale l, String format, Object... args) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.format(l, format, args);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
