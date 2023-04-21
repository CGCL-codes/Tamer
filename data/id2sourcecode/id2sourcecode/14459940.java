    @Override
    public void print(char[] s) {
        try {
            writerListLock.readLock().lock();
            for (PrintWriter writer : writerList) {
                writer.print(s);
            }
        } finally {
            writerListLock.readLock().unlock();
        }
    }
