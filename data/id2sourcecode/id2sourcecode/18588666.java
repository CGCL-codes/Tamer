    @Override
    public void println() {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.println();
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
