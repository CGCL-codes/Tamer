    public synchronized void lockWrite() throws IOException {
        final Thread currentThread = Thread.currentThread();
        if (writer_ == null) {
            writer_ = currentThread;
        }
        while (!canWrite()) {
            try {
                wait();
            } catch (final InterruptedException e) {
                throw (IOException) new IOException().initCause(e);
            }
            if (writer_ == null) {
                writer_ = currentThread;
            }
        }
        if (writer_ == null) {
            writer_ = currentThread;
        }
        assertTrue("The current thread is not the writer", writer_ == currentThread);
        assertTrue("There are read locks not belonging to the current thread.", canRead());
        writeLocks_++;
        FuLog.trace("CSG : " + currentThread.getName() + " is getting write lock:" + writeLocks_);
    }
