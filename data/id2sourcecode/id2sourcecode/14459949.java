    @Override
    public void println() {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.println();
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
