        protected void releaseWritingLock() {
            synchronized (lock) {
                if (Thread.currentThread() == writerThread) {
                    writerThread = null;
                }
                lock.notifyAll();
            }
        }
