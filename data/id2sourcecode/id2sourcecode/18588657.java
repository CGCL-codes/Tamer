    @Override
    public void print(char[] s) {
        try {
            writerListLock.readLock().lock();
            for (PrintStream writer : writerList) {
                writer.print(s);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
