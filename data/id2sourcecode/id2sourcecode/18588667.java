    @Override
    public void println(boolean x) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.println(x);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
