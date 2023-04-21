    @Override
    public PrintStream append(CharSequence csq) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.append(csq);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
