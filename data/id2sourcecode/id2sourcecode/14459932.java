    @Override
    public PrintWriter append(CharSequence csq, int start, int end) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.append(csq, start, end);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
