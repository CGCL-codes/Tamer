    public static void writerThread() {
        new Thread() {

            public void run() {
                testLock.waitWriteLock();
                try {
                    delay(1000);
                    System.err.println("I have the write lock!");
                    delay(5000);
                } finally {
                    testLock.releaseWriteLock();
                }
            }
        }.start();
    }
