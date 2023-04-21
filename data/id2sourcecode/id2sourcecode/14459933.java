    @Override
    public PrintWriter append(CharSequence csq) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.append(csq);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
        return this;
    }
