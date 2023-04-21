    public static void writerThread2() {
        new Thread() {

            public void run() {
                testLock.waitWriteLock(2000);
                try {
                    delay(1000);
                    System.err.println("I have the write lock 2!");
                    delay(5000);
                } finally {
                    testLock.releaseWriteLock();
                }
            }
        }.start();
    }
